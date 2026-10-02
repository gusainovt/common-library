# Библиотека с универсальным кодом для всех проектов на JAVA
- [Как добавить в проект](#как-добавить-в-проект)

## Как добавить в проект

Инструкция для maven, блоки ниже добавить в файл `pom.xml`

### Добавить JitPack repository

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

#### Добавить саму библиотеку: 

```xml
	<dependency>
	    <groupId>com.github.gusainovt</groupId>
	    <artifactId>common-library</artifactId>
	    <version>1.0.0</version>
	</dependency>
```

Другие спосообы подключения можно посмотреть здесь: 

[![JitPack](https://jitpack.io/v/gusainovt/common-library.svg)](https://jitpack.io/#gusainovt/common-library)
