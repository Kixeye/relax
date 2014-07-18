Relax
=====
[![Build Status](https://travis-ci.org/Kixeye/relax.svg?branch=master)](https://travis-ci.org/Kixeye/relax)

A async REST client.

Getting Started
==========

The class to start with is [RestClients]. [RestClients] is a builder for constructing objects the implement the [RestClient] interface.

You can use the default values in [RestClients], but it is recommended you optimize those attributes.

Example
==========

```java
RestClientSerDe serDe = new RestClientSerDe() {
	@Override
	public byte[] serialize(String mimeType, Object obj) throws IOException {
		return ((String)obj).getBytes(StandardCharsets.UTF_8);
	}
	
	@Override
	public <T> T deserialize(String mimeType, byte[] data, int offset, int length, Class<T> clazz) throws IOException {
		return new String(data, offset, length, StandardCharsets.UTF_8);
	}
};

try (RestClient client = RestClients.create(serDe).build()) {
	String responseString = client.get("http://localhost:" + port + "/test", null, String.class).waitForComplete(5, TimeUnit.SECONDS).get().getBody().deserialize();
}
```

## Binaries

Example for Maven:

```xml
<dependency>
    <groupId>com.kixeye.relax</groupId>
    <artifactId>relax</artifactId>
    <version>x.y.z</version>
</dependency>
```
```
and for Ivy:

```xml
<dependency org="com.kixeye.relax" name="relax" rev="x.y.z" />
```
and for Gradle:

```groovy
compile 'com.kixeye.relax:relax:x.y.z'
```

## Build

To build:

```
$ git clone git@github.com:KIXEYE/relax.git
$ cd relax/
$ mvn clean package
```

## Bugs and Feedback

For bugs, questions and discussions please use the [Github Issues](https://github.com/KIXEYE/relax/issues).

 
## LICENSE

Copyright 2014 KIXEYE, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

<http://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.