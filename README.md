# Rekords in Java &nbsp; [![Build Status](https://travis-ci.org/SamirTalwar/Rekord.png)](https://travis-ci.org/SamirTalwar/Rekord)

A rekord is an immutable data structure of key-value pairs. Kind of like an immutable map of objects, but completely type-safe, as the keys themselves contain the type information of the value.

It can be used as an alternative to classes with getters (immutable beans, if you will) so you don't have to implement a new concrete class for every value concept—instead, a single type has you covered. You also get builders for free, `equals` and `hashCode` are implemented for you, and other concepts, such as validation and default values, can be implemented once and used for all rekords. Finally, all Rekords, being immutable, are thread-safe to construct and to use.

And there's no magic.

An example:

```java
Rekord<Sandvich> sandvich = Sandvich.rekord
        .with(Sandvich.filling, Lettuce)
        .with(Sandvich.style, Burger);

assertThat(sandvich.get(Sandvich.filling), is(Filling.Lettuce));
assertThat(sandvich.get(Sandvich.style), is(Style.Burger));
assertThat(sandvich.get(Sandvich.bread), is(Bread.Brown));
```

The magic is really in the key. It's defined as follows:

```java
public static interface Sandvich {
    Key<Sandvich, Filling> filling = Key.named("filling");
    Key<Sandvich, Bread> bread = Key.named("bread").that(defaultsTo(Brown));
    Key<Sandvich, Style> style = Key.named("style");

    Rekord<Sandvich> rekord = Rekord.of(Sandvich.class).accepting(filling, bread, style);

    // ...
}
```

So all you need is one interface and a few constants. The return type of the `Rekord::get` method is the type embodied in the key, so for the sandvich filling, the return type is `Filling`.

More examples can be found [in the tests][Tests].

[Tests]: https://github.com/SamirTalwar/Rekord/tree/master/src/test/java/com/noodlesandwich/rekord

### Installation

You can use Rekord v0.1 by dropping the following into your Maven `pom.xml`. It's in Maven Central.

```xml
<dependency>
    <groupId>com.noodlesandwich</groupId>
    <artifactId>rekord</artifactId>
    <version>0.1</version>
</dependency>
```

If you're not using Maven, alter as appropriate for your dependency management system. If you just want a JAR, you can [download it directly from Maven][rekord-0.1.jar].

[rekord-0.1.jar]: http://search.maven.org/remotecontent?filepath=com/noodlesandwich/rekord/0.1/rekord-0.1.jar

### Why "Rekord"?

I was in Germany, at [SoCraTes 2013][SoCraTes Conference], when I named it. So I thought I'd make the name a little more German. ;-)

[SoCraTes Conference]: http://www.socrates-conference.de/

### Credits

Thanks go to:

* [Nat Pryce][@natpryce], for coming up with the idea of "key" objects in [Make It Easy][].
* [Dominic Fox][@domfox], for extending the idea by delegating to a simple map in [karg][].
* Quentin Spencer-Harper, for working with me on the initial implementation of this library.

[@natpryce]: https://twitter.com/natpryce
[@domfox]: https://twitter.com/domfox
[Make It Easy]: https://code.google.com/p/make-it-easy/
[karg]: https://github.com/youdevise/karg
