# Rekords in Java &nbsp; [![Build Status](https://travis-ci.org/SamirTalwar/Rekord.png)](https://travis-ci.org/SamirTalwar/Rekord)

A rekord is an immutable data structure of key-value pairs. Kind of like an immutable map of objects, but completely
type-safe, as the keys themselves contain the type information of the value.

### Why?

Duplication is difficult to exterminate in Java code. In particular, one type of structural duplication is scattered
throughout our software. It looks something like this:

```java
public class Person {
    private final String firstName;
    private final String lastName;
    private final LocalDate dateOfBirth;
    private final Address address;

    public Person(String firstName, String lastName,
                  LocalDate dateOfBirth, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    // I can't go on. You know the rest.
}
```

Of course, that's not all. We then have to make a [builder][Make It Easy], a [matcher][Hamcrest] for readable test
cases, and everything else to support this, the dumbest of all classes.

OK, now we can use our `Person` type. It's beautiful, right? It just needs some annotations to serialize to JSON, then
some [JPA][Java Persistence API] annotations for persistence to the database, and…

**UGH.**

### Rekord to the Rescue

Code like the above makes me angry. It's such a waste of space. The same thing, over and over again.

With Rekord, the above suddenly becomes a lot smaller.
       
```java
public interface Person {
    Key<Person, String> firstName = SimpleKey.named("first name");
    Key<Person, String> lastName = SimpleKey.named("last name");
    Key<Person, LocalDate> dateOfBirth = SimpleKey.named("date of birth");
    Key<Person, FixedRekord<Address>> address = RekordKey.named("address");

    Rekord<Person> rekord = Rekord.of(Person.class)
        .accepting(firstName, lastName, dateOfBirth, address);
}
```

That `Rekord<Person>` object is a *rekord builder*. You can construct new people with it. Like so:

```java
Rekord<Person> woz = Person.rekord
   .with(Person.firstName, "Steve")
   .with(Person.lastName, "Wozniak")
   .with(Person.dateOfBirth, LocalDate.of(1950, 8, 11))
   .with(Person.address, Address.rekord
       .with(Address.city, "Cupertino"));
```

`woz` has the type `Rekord<Person>`, but you can treat it basically as if it were a `Person` as shown above. There's
only one real difference. Instead of:

```java
woz.getFirstName()
```

You call:

```java
woz.get(Person.firstName)
```

Simple, right?

### What else?

Rekord is deigned to be used as an alternative to classes with getters (immutable beans, if you will) so you don't have
to implement a new concrete class for every value concept—instead, a single type has you covered.
 
For free, you also get:

  * [builders][Make It Easy]
  * [matchers][Hamcrest]
  * validation
  * serialization
  * transformations
  * `equals` and `hashCode`
  * `toString`

#### Builders

Every Rekord is also a **builder**. Rekords themselves are immutable, so the `with` method returns a new
Rekord each time. Use them, pass them around, make new rekords out of them; because they don't mutate, they're perfectly
safe.

#### Matchers

There are [**matchers**][Hamcrest] for the builders. You can assert that a rekord conforms to a specific specification,
just check they have specific keys, or anywhere in between. Take a look at [`RekordMatchers`][RekordMatchers.java] for
more information.

```java
Rekord<Person> steve = Person.rekord
    .with(Person.firstName, "Steve")
    .with(Person.lastName, "Wozniak")
    .with(Person.dateOfBirth, LocalDate.of(1950, 8, 11));

assertThat(steve, is(aRekordOf(Person.class)
    .with(Person.firstName, equalToIgnoringCase("steVE"))
    .with(Person.lastName, containsString("Woz"))));

assertThat(steve, hasProperty(Person.dateOfBirth, lessThan(LocalDate.of(1970, 1, 1))));
```

#### Validation

The matchers play into **validation**. Rather than just building a rekord and using it, you can also create a
[`ValidatingRekord`][ValidatingRekordTest.java] which allows you to build a rekord up, then ensure it passes a
specification.
 
The same matchers you can use in your tests are used for validation.

When you `fix` a validating rekord, one of two things happen. It will either return a `ValidRekord`, which implements
the `FixedRekord` interface, providing you the `get` method (and a few others), or it will throw an
`InvalidRecordException`. Because we use Hamcrest matchers, the exception should have a decent error message which
explains why the validation failed.

```java
ValidatingRekord<Person> namedPerson = ValidatingRekord.validating(Person.rekord)
    .expecting(hasProperties(Person.firstName, Person.lastName));
    
ValidRekord<Person> steve = namedPerson
    .with(Person.lastName, "Wozniak")
    .with(Person.dateOfBirth, LocalDate.of(1950, 8, 11))
    .fix(); // throws InvalidRekordException
```

#### Transformation

Rekord properties can be transformed on storage and on retrieval. The *rekord-keys* library adds a number of keys that
wrap existing keys. As of the time of writing, you can:

  * Specify a default value for a key with [`DefaultedKey`][DefaultedKeyTest.java]
  * Apply an arbitrary (reversible) transformation with [`FunctionKey`][FunctionKeyTest.java]
  * Break a value into many values with [`OneToManyKey`][OneToManyKeyTest.java]
  * Dive into rekords several layers deep with [`ComposedKey`][ComposedKeyTest.java]
  * Rename a key with [`RenamedKey`][RenamedKeyTest.java]
  
[ComposedKeyTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/keys/src/test/java/com/noodlesandwich/rekord/keys/ComposedKeyTest.java
[DefaultedKeyTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/keys/src/test/java/com/noodlesandwich/rekord/keys/DefaultedKeyTest.java
[FunctionKeyTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/keys/src/test/java/com/noodlesandwich/rekord/keys/FunctionKeyTest.java
[OneToManyKeyTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/keys/src/test/java/com/noodlesandwich/rekord/keys/OneToManyKeyTest.java
[RenamedKeyTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/keys/src/test/java/com/noodlesandwich/rekord/keys/RenamedKeyTest.java

#### Serialization

Finally, rekords can be **serialized**. Whether you want it to be JSON, XML or just a Java map, we've got you covered.
It's pretty simple. For example:

```java
Rekord<Person> spongebob = Person.rekord
        .with(Person.firstName, "Spongebob")
        .with(Person.lastName, "Squarepants");

Document document = spongebob.serialize(new DomXmlSerializer());

assertThat(the(document), isSimilarTo(the(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<person>" +
        "    <first-name>Spongebob</first-name>" +
        "    <last-name>Squarepants</last-name>" +
        "</person>")));
```

The available serializers are, at the time of writing: 

  * `StringSerializer`, which is used by `Rekord::toString` to create a string representation of a rekord
  * `MapSerializer`, which converts a Rekord into a `Map<String, Object>`
  * `DomXmlSerializer`, which converts a Rekord into a `Document` object. It's demonstrated above
  * `JacksonSerializer`, which converts a Rekord into JSON, and can either return a `String` or write it directly to a
    `Writer`
    
Note: to use `JacksonSerializer`, you'll need to include `rekord-jackson` as a separate dependency. This is to avoid
including the [Jackson JSON Processor][] as a dependency of Rekord.

There's almost certainly a bunch of stuff we haven't covered. More examples can be found [in the tests][Tests].

[RekordMatchers.java]: https://github.com/SamirTalwar/Rekord/blob/master/validation/src/main/java/com/noodlesandwich/rekord/validation/RekordMatchers.java
[ValidatingRekordTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/validation/src/test/java/com/noodlesandwich/rekord/validation/ValidatingRekordTest.java
[Tests]: https://github.com/SamirTalwar/Rekord/tree/master/core/src/test/java/com/noodlesandwich/rekord

## Installation

You can use Rekord v0.2 by dropping the following into your Maven `pom.xml`. It's in Maven Central.

```xml
<dependency>
    <groupId>com.noodlesandwich</groupId>
    <artifactId>rekord</artifactId>
    <version>0.2</version>
</dependency>
```

If you're not using Maven, alter as appropriate for your dependency management system. If you just want a JAR, you can
[download it directly from Maven][rekord-0.2.jar].

[rekord-0.2.jar]: http://search.maven.org/remotecontent?filepath=com/noodlesandwich/rekord/0.2/rekord-0.2.jar

## Why "Rekord"?

I was in Germany, at [SoCraTes 2013][SoCraTes Conference], when I named it. So I thought I'd make the name a little more
German. ;-)

[SoCraTes Conference]: http://www.socrates-conference.de/

## Credits

Thanks go to:

* [Nat Pryce][@natpryce], for coming up with the idea of "key" objects in [Make It Easy][].
* [Dominic Fox][@domfox], for extending the idea by delegating to a simple map in [karg][].
* Quentin Spencer-Harper, for working with me on the initial implementation of this library.

[@natpryce]: https://twitter.com/natpryce
[@domfox]: https://twitter.com/domfox

[Hamcrest]: https://github.com/hamcrest/JavaHamcrest
[Jackson JSON Processor]: http://jackson.codehaus.org/
[karg]: https://github.com/youdevise/karg
[Make It Easy]: https://code.google.com/p/make-it-easy/
