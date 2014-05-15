# Rekords in Java &nbsp; [![Build Status](https://travis-ci.org/SamirTalwar/Rekord.png)](https://travis-ci.org/SamirTalwar/Rekord)

A rekord is an immutable data structure of key-value pairs. Kind of like an immutable map of objects, but completely
type-safe, as the keys themselves contain the type information of the value.

It can be used as an alternative to classes with getters (immutable beans, if you will) so you don't have to implement a
new concrete class for every value concept—instead, a single type has you covered. You also get builders for free,
`equals` and `hashCode` are implemented for you, validation and serialization are covered, and other concepts, such as
default values, can be implemented once and used for all rekords. Finally, all Rekords, being immutable, are thread-safe
to construct and to use.

And there's no magic.

An example:

```java
Rekord<Sandvich> sandvich = Sandvich.rekord
        .with(Sandvich.filling, Lettuce)
        .with(Sandvich.style, Burger);

assertThat(sandvich.get(Sandvich.bread), is(Brown));
assertThat(sandvich.get(Sandvich.filling), is(Lettuce));
assertThat(sandvich.get(Sandvich.style), is(Burger));
```

How's that work? And why is the bread brown? We didn't specify that.

The magic is really in the key. It's defined as follows:

```java
public interface Sandvich {
    Key<Sandvich, Bread> bread = Key.named("bread").that(defaultsTo(Brown));
    Key<Sandvich, Filling> filling = Key.named("filling");
    Key<Sandvich, Style> style = Key.named("style");

    Rekord<Sandvich> rekord = Rekord.of(Sandvich.class).accepting(filling, bread, style);
}
```

So all you need is one interface and a few constants. The return type of the `Rekord::get` method is the type embodied
in the key, so for the sandvich filling, the return type is `Filling`.

### What else?

There's more. Every Rekord is also a **builder**. Rekords themselves are immutable, so the `with` method returns a new
Rekord each time. Use them, pass them around, make new rekords out of them; because they don't mutate, they're perfectly
safe.

There are [**matchers**][Hamcrest] for the builders. You can assert that a rekord conforms to a specific specification,
just check they have specific keys, or anywhere in between. Take a look at [`RekordMatchers`][RekordMatchers.java] for
more information.

This plays into **validation**. Rather than just building a rekord and using it, you can also create a
[`ValidatingRekord`][ValidatingRekordTest.java] which allows you to build a rekord up, then ensure it passes a
specification. Just like the matchers, Hamcrest is used for validation.

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

[Tests]: https://github.com/SamirTalwar/Rekord/tree/master/src/test/java/com/noodlesandwich/rekord
[RekordMatchers.java]: https://github.com/SamirTalwar/Rekord/blob/master/src/main/java/com/noodlesandwich/rekord/validation/RekordMatchers.java
[ValidatingRekordTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/src/test/java/com/noodlesandwich/rekord/validation/ValidatingRekordTest.java
[serialization]: https://github.com/SamirTalwar/Rekord/tree/master/src/main/java/com/noodlesandwich/rekord/serialization
[extra]: https://github.com/SamirTalwar/Rekord/tree/master/src/main/java/com/noodlesandwich/rekord/extra

[Guava]: https://code.google.com/p/guava-libraries/
[Hamcrest]: https://github.com/hamcrest/JavaHamcrest
[Jackson]: http://jackson.codehaus.org/

## Installation

You can use Rekord v0.1 by dropping the following into your Maven `pom.xml`. It's in Maven Central.

```xml
<dependency>
    <groupId>com.noodlesandwich</groupId>
    <artifactId>rekord</artifactId>
    <version>0.1</version>
</dependency>
```

If you're not using Maven, alter as appropriate for your dependency management system. If you just want a JAR, you can
[download it directly from Maven][rekord-0.1.jar].

[rekord-0.1.jar]: http://search.maven.org/remotecontent?filepath=com/noodlesandwich/rekord/0.1/rekord-0.1.jar

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
[Make It Easy]: https://code.google.com/p/make-it-easy/
[karg]: https://github.com/youdevise/karg
