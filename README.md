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

`woz` has the type `Rekord<Person>`, but you can treat it basically as if it were a `Person` as shown above. There's only one real difference. Instead of:

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

  * [builders][TODO]
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

#### Validation

This plays into **validation**. Rather than just building a rekord and using it, you can also create a
[`ValidatingRekord`][ValidatingRekordTest.java] which allows you to build a rekord up, then ensure it passes a
specification. Just like the matchers, Hamcrest is used for validation.

#### Transformation

Rekord properties can be transformed on storage and on retrieval. The *rekord-keys* library adds a number of keys that
wrap existing keys. As of the time of writing, you can:

  * Specify a default value for a key with `DefaultedKey`
  * Apply an arbitrary (reversible) transformation with `FunctionKey`
  * Break a value into many values with `OneToManyKey`
  * Dive into rekords several layers deep with `ComposedKey`
  * Rename a key with `RenamedKey`

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

You can see the list of serializers in the [`serialization`][serialization] package. If you don't spot the one you're
looking for, just implement your own. The API is fairly simple.

There are a couple of extra pieces of functionality in the [`extra`][extra] package. At the moment, there are
transformers that use [Guava][], and a serializer that uses [Jackson][]. They're hidden away because you'll get
compilation failures if you try and use them without the correct JAR dependencies. If you're interested, grab the
libraries and get going.

There's almost certainly a bunch of stuff we haven't covered. More examples can be found [in the tests][Tests].

[Tests]: https://github.com/SamirTalwar/Rekord/tree/master/core/src/test/java/com/noodlesandwich/rekord
[RekordMatchers.java]: https://github.com/SamirTalwar/Rekord/blob/master/validation/src/main/java/com/noodlesandwich/rekord/validation/RekordMatchers.java
[ValidatingRekordTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/validation/src/test/java/com/noodlesandwich/rekord/validation/ValidatingRekordTest.java
[serialization]: https://github.com/SamirTalwar/Rekord/tree/master/core/src/main/java/com/noodlesandwich/rekord/serialization
[extra]: https://github.com/SamirTalwar/Rekord/tree/master/core/src/main/java/com/noodlesandwich/rekord/extra

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

[Guava]: https://code.google.com/p/guava-libraries/
[Hamcrest]: https://github.com/hamcrest/JavaHamcrest
[Jackson]: http://jackson.codehaus.org/
[karg]: https://github.com/youdevise/karg
[Make It Easy]: https://code.google.com/p/make-it-easy/
