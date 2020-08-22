# danger-checkstyle-plugin
![](https://github.com/uzzu/danger-checkstyle-plugin/workflows/master/badge.svg) [![ktlint](https://img.shields.io/badge/code%20style-%E2%9D%A4-FF4081.svg)](https://ktlint.github.io/) [![Download](https://api.bintray.com/packages/uzzu/maven/danger-checkstyle-plugin/images/download.svg)](https://bintray.com/uzzu/maven/danger-checkstyle-plugin/_latestVersion)

A [Danger Kotlin](https://github.com/danger/kotlin) plugin for reporting [checkstyle](https://checkstyle.org/) result.

## Setup

- [Install Danger Kotlin](https://github.com/danger/kotlin#setup)
- Add the following dependency in your `Dangerfile.df.kts` :
  ```kotlin
  @file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.1.0")
  ```
- Register plugin and then write script to report your checkstyle result. 
  ```kotlin
  import co.uzzu.danger.plugins.checkstyle.CheckStyle
  import systems.danger.kotlin.Danger
  import systems.danger.kotlin.register

  register plugin CheckStyle

  // :
  ```

## Examples

### Simple use

```kotlin
@file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.1.0")

import co.uzzu.danger.plugins.checkstyle.CheckStyle
import systems.danger.kotlin.Danger
import systems.danger.kotlin.register

register plugin CheckStyle

val d = Danger(args)

CheckStyle.report("path/to/checkstyle_result.xml")
```

### Use with glob matcher

```kotlin
@file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.1.0")

import co.uzzu.danger.plugins.checkstyle.CheckStyle
import systems.danger.kotlin.Danger
import systems.danger.kotlin.register

register plugin CheckStyle

val d = Danger(args)

CheckStyle.report("glob:**/path/to/*check_result.xml")
```

### Use with configuration block

```kotlin
@file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.1.0")

import co.uzzu.danger.plugins.checkstyle.CheckStyle
import systems.danger.kotlin.Danger
import systems.danger.kotlin.register

register plugin CheckStyle

val d = Danger(args)

CheckStyle.report {
    // Set configuration properties 
    // :

    // [Required] Set path to checkstyle result file.
    path = "path/to/checkstyle_result.xml"
}
```

## Configuration

You can customize reporting styles as you needed by using configuration scope.
The CheckStyle plugin object also has same properties, and these properties are used as default of configuration scope.

### reporter

Determinate the reporting style. You can choose the following reporter.

#### Inline

Report as inline comment
You can choose reporting method `MESSAGE`, `WARN`, and `FAIL` .

```kotlin
import co.uzzu.danger.plugins.checkstyle.Inline
import co.uzzu.danger.plugins.checkstyle.ReportMethod.*

CheckStyle.report = Inline // reporterMethod = ERROR is used by default.
CheckStyle.report = Inline { reportMethod = WARN }
```

#### Markdown

Report as message with the Markdown format.

```kotlin
import co.uzzu.danger.plugins.checkstyle.Markdown

CheckStyle.report = Markdown // label = "Checkstyle" is used by default.
CheckStyle.report = Markdown { label =  "My checkstyle" }
```

### severities

Determinate list of the checkstyle severities(`ERROR`, `IGNORE`, `INFO`, `WARNING` ) to report.
Only `ERROR` is reported by default.

```kotlin
import co.uzzu.danger.plugins.checkstyle.Severity.*

CheckStyle.severities = listOf(ERROR, IGNORE, INFO, WARNING)
```

### basePath

Determinate base of check style result file path.
The working directory is used by default.

```kotlin
CheckStyle.basePath = "path/to/your/base/directory"
```

## License

[Apache 2.0 License](LICENSE.txt)
