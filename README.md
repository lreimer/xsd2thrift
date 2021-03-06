# XSD2THRIFT [![Build Status](https://travis-ci.org/tranchis/xsd2thrift.png?branch=master)](https://travis-ci.org/tranchis/xsd2thrift)

This tool allows for converting XML Schema files (.xsd) to Thrift (.thrift) and Protocol Buffers 2 /3 (.proto).

## Binary download

[Version 1.0](http://dl.bintray.com/content/tranchis/xsd2thrift/com/github/tranchis/xsd2thrift/1.0/xsd2thrift-1.0-onejar.jar?direct)

## Importing from Maven

    <repositories>
      <repository>
        <id>xsd2thrift</id>
        <url>http://dl.bintray.com/content/tranchis/xsd2thrift</url>
      </repository>
    </repositories>
    
    <dependency>
      <groupid>com.github.tranchis</groupid>
      <artifactid>xsd2thrift</artifactid>
      <version>1.0</version>
      <type>jar</type>
    </dependency>

## Building

To build xsd2thrift, you need Maven (http://maven.apache.org/) installed. To build xsd2thrift, run `mvn install`.

xsd2thrift has been tested on Java SE 6.

## Usage

```bash
java -jar xsd2thrift-1.0.jar [--thrift] [--protobuf] [--output=FILENAME] [--package=NAME]
                        [--nestEnums=true|false] [--splitBySchema=true|false] filename.xsd
  --thrift                   : convert to Thrift
  --protobuf                 : convert to Protocol Buffers 2
  --proto2                   : convert to Protocol Buffers 2
  --proto3                   : convert to Protocol Buffers 3
  --filename=FILENAME        : store the result in FILENAME instead of standard output
  --package=NAME             : set namespace/package of the output file
  --nestEnums=true|false     : nest enum declaration within messages that reference them, only supported by protobuf, defaults to true
  --splitBySchema=true|false : split output into namespace-specific files, defaults to false
```

## License

The code contributed for this package is licensed under LGPL v3 (see LICENSE).

XSOM is covered by GPL v2 with classpath exception (see LICENSE-xsom.1).

XSOM internally uses an URI class, which is copyrighted by Thai Open Source
Center (see LICENSE-xsom.2).

## Contact

Any feedback will be greatly appreciated, at the GitHub project page
(http://github.com/tranchis/xsd2thrift) or at tranchis_-_AT_-_gmail.com.

## Contributors (@github.com)

* tranchis
* p14n
* Fred-dy-
* pfisterer
* wesyoung
* dabble
* ae589

