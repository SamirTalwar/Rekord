# Rekords in Java

A rekord is an immutable data structure of key-value pairs. Kind of like an immutable map of objects, but completely type-safe, as the keys themselves contain the type information of the value.

It can be used as an alternative to classes with getters (immutable beans, if you will) so you don't have to implement a new concrete class for every value concept—instead, a single type has you covered. You also get a builder and a Hamcrest matcher for free, and other concepts, such as validation and default values, can be implemented once and used for all rekords.

And there's no magic.

An example:

    Rekord<Sandvich> sandvich = Rekord.<Sandvich>create()
            .with(Sandvich.filling, Lettuce)
            .with(Sandvich.bread, Brown)
            .with(Sandvich.style, Burger)
            .build();

    assertThat(sandvich.get(Sandvich.filling), is(Lettuce));

The magic is really in the key. It's defined as follows:

    public static interface Sandvich extends RekordType {
        Key<Sandvich, Filling> filling = key();
        Key<Sandvich, Bread> bread = key();
        Key<Sandvich, Style> style = key();

        ...
    }

So all you need is one interface and a few constants. The return type of the `Rekord::get` method is the type embodied in the key, so for the sandvich filling, the return type is `Filling`.

More examples can be found [in the tests][RekordTest.java].

[RekordTest.java]: https://github.com/SamirTalwar/Rekord/blob/master/src/test/java/com/noodlesandwich/rekord/RekordTest.java

### Why "Rekord"?

I was in Germany, at [SoCraTes 2013][SoCraTes Conference], when I named it. So I thought I'd make the name a little more German. ;-)

[SoCraTes Conference]: http://www.socrates-conference.de/
