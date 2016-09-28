# Supported Expressions

## Aggregate

All aggregate expressions take a single argument and output a single value.

* CreateHll - Creates a bitmap representing a HyperLogLog object using the Clearspring implementation
* CreateHllp - Similar to CreateHll, but uses the HyperLogLogPlus algorithm
* Max - Returns the maximum value in a set of objects. If the input values are numbers, clojure.lang.Numbers.max() is used, otherwise Java's Comparable interface is used to perform comparisons. Objects that do not implement Comparable are not supported.
* Sum - Sums a set of numbers into a single value. Works similarly to Sum in SQL. Uses Trident's built in Sum aggregator.

## Transform

Conditionals

* If - `[<PREDICATE>,<TRUE_VALUE>,<FALSE_VALUE>]` - Returns \<TRUE_VALUE\> if \<PREDICATE\> is true, otherwise returns \<FALSE_VALUE\>

Date/Time

* FormatDate - `[<DATE>,<OUTPUT_DATE_FORMAT>]` - Takes \<DATE\> and formats it into a string using \<OUTPUT_DATE_FORMAT\>. Uses Java's SimpleDateFormat and the date is adjusted to UTC.
* GetTime - `[<DATE>]` - Turns a date object into a Long timestamp using the getTime method.
* ParseDate - `[<DATE_STRING>,<INPUT_DATE_FORMAT>]` - Takes \<DATE_STRING\> and parses it into a Date object using \<OUTPUT_DATE_FORMAT\>. Uses Java's SimpleDateFormat and the date is adjusted to UTC.
* RoundDate - `[<DATE>,<AMOUNT>,<UNIT>]` - Takes a date object and rounds it to the nearest \<AMOUNT\> \<UNIT\>. For example, if \<AMOUNT\> = 10 and \<UNIT\> = Second, then it will round to the nearest 10th second. Allowed units are Second, Minute, Hour, and Day. 

Math

* + - Addition
* / - Division
* % - Modulus
* * - Multiplication
* - - Subtraction

Predicate

* = - `[<VALUE_A>,<VALUE_B>]` - Compares \<VALUE_A\> to \<VALUE_B\>. If both values are Numbers, clojure.lang.Numbers.equiv is used to perform the comparison. Otherwise, the equals method of \<VALUE_A\> is called.
* In - Returns true if the first argument is equal to one of the remaining arguments. Uses clojure.lang.Numbers.equiv for Number objects, or the equals method for other object types. Similar to the SQL keyword of the same name.
* Logical Operators - And, Not, Or, Xor - Each takes 2 or more arguments, except for Not, which only takes one. If more than 2 arguments is supplied for And/Or/Xor, the operator is chained, i.e. `{"And":[A,B,C,D]}` is the same as `{"And":[{"And":[{"And":[A,B]},C]},D]}` is the same as `A and B and C and D`).
* Numerical Comparators - >, >=, <, <=, != - Each takes 2 arguments. Uses clojure.lang.Numbers to perform the comparisons.
* RLike - `[<STRING>,<REG_EX>]` - Similar to RLike in MySQL or Hive. Uses the String method matches to evaluate the regular expression. The function caches compilations of the `<REG_EX>` values it sees, though typically that expression is a constant.

Map

* ExpandKeys - Takes a Java Map object and emits each key. Unlike most other transformation expressions which add a single value onto a tuple, this takes an input tuple and creates a new tuple for each key. 
* ExpandValues - Takes a Java Map object and emits each value. Similar to ExpandKeys, this can create multiple tuples for each input tuple.

Other

* Hash - `[<ALGORITHM_NAME>,<VALUE>]` - Hashes the value using the given algorithm. The hash is returned as a byte array. Supported algorithms:
    * Murmur2 - Uses Clearspring's Murmur2 implementation. The output is always 8 bytes.

## Adding new expressions

Adding a new expressions requires the following changes:

* Sub-classing a new expression from the appropriate parent class (AggregationExpression, TransformExpression, or PredicateExpression) and overriding the abstract methods. These classes allow the query engine to parse and operate on queries and to interact with the input streams.
* Updating the /META-INF/services/com.jwplayer.sqe.language.expression.FunctionExpression resource file to include the expressions class. You can also add "UDFs" to a project that includes SQE as a dependency by adding a new /META-INF/services/com.jwplayer.sqe.language.expression.FunctionExpression resource file in that project. Expression names used in queries are pulled from the getFunctionName method in each expression's class. These names are case insensitive to the parser. Because SQE includes a version of this file, you'll need to use something like Maven's Shade plugin (https://maven.apache.org/plugins/maven-shade-plugin/examples/resource-transformers.html#AppendingTransformer) to combine the original resource file, with your custom resource file to combine the base SQE expressions and your UDFs.
* Creating new classes from the Trident BaseAggregator, CombinerAggregator, ReducerAggregator or BaseFunction classes. These classes operate directly on the stream. One or more expressions will parse to one of the supported expression types, which in turn interface with one or more Trident Aggregators or Functions.

When writing new expressions, it shouldn't be important to know what types of expressions the arguments are. Field expressions should be handled naturally because the values of that field will come in through each tuple you process. Constant and Transform expressions that are nested in the argument list will appear in tuples the same as a field. Behind the scenes, SQE processes nested transform expressions first. By the time a transform function higher up in an expression tree is processed, its transform function arguments already appear as fields in the tuple. 