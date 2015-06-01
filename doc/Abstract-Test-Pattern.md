# Abstract Test Pattern

Mit dem Abstract Test Pattern lassen sich Schnittstellenverträge (d.h. Interfaces) und abstrakte Klassen 
testen. Damit kann sichergestellt werden, dass Subklassen (bzw. Klassen, die ein gegebenes Interface
implementieren) sich an den vereinbarten Vertrag halten.

## Testen des Schnittstellenvertrags von equals

Im JDK ist für die Methode `Object.equals` ein recht umfangreiche Vertrag im JavaDoc formuliert. Hier der wichtigste
 Teil als Ausschnitt:

```java
/**
 * Indicates whether some other object is "equal to" this one.
 * <p>
 * The {@code equals} method implements an equivalence relation
 * on non-null object references:
 * <ul>
 * <li>It is <i>reflexive</i>: for any non-null reference value
 *     {@code x}, {@code x.equals(x)} should return
 *     {@code true}.
 * <li>For any non-null reference value {@code x},
 *     {@code x.equals(null)} should return {@code false}.
 * </ul>
 * [...]
 */
```

Diesen Vertrag müssen alle Klassen einhalten, die die `equals` Methode überschreiben. Um dies sicherzustellen,
lässt sich dieser JavaDoc Kommentar in einen äquivalenten Test umwandeln:

```java
/**
 * Verifies that objects of any Java class comply with the contract in {@link Object#equals(Object)}.
 */
public abstract class AbstractEqualsTest {
    /**
     * Creates the subject under test.
     *
     * @return the SUT
     */
    protected abstract Object createSut();

    /**
     * Verifies that equals is <i>reflexive</i>: for any non-null reference value {@code x}, {@code x.equals(x)} should
     * return {@code true}.
     */
    @Test
    public void shouldReturnTrueOnEqualsThis() {
        Object sut = createSut();

        assertThat(sut.equals(sut)).isTrue();
    }

    /**
     * Verifies that for any non-null reference value {@code x}, {@code x.equals(null)} should return {@code false}.
     */
    @Test
    public void shouldReturnFalseOnEqualsNull() {
        assertThat(createSut().equals(null)).isFalse();
    }
}
```

Jede Klasse, die `equals` überschreibt, kann den Schnittstellenvertrag mit folgenden 2 Schritten prüfen:

1. Erzeugen einer Subklasse von [AbstractEqualsTest](../../master/src/test/java/edu/hm/hafner/util/AbstractEqualsTest.java).
2. Überschrieben der Factory Method `createSut`.

Die restlichen Tests der zu überprüfenden Klassen werden anschließend wie gewohnt in der Testklasse kodiert.

## Testen des Comparable Schnittstellenvertrags 

Das gleiche Verfahren lässt sich auch mit Interfaces umsetzen. 
Z.B. muss das Interface `Comparable` so implementiert werden, dass die Operation `compareTo` symmetrisch ist:

```java
/**
 * Compares this object with the specified object for order.  Returns a
 * negative integer, zero, or a positive integer as this object is less
 * than, equal to, or greater than the specified object.
 *
 * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
 * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
 * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
 * <tt>y.compareTo(x)</tt> throws an exception.)
 * [...]
 */
public int compareTo(T o);
```

Ein dazu passender abstrakter Test könnte als 
[AbstractComparableTest](../../master/src/test/java/edu/hm/hafner/util/AbstractComparableTest.java) 
folgendermaßen umgesetzt werden: 

```java
/**
 * Verifies that comparable objects comply with the contract in {@link Comparable#compareTo(Object)}.
 */
public abstract class AbstractComparableTest <T extends Comparable<T>> {
    /**
     * Verifies that a negative integer, zero, or a positive integer as this object is less than, equal to, or greater
     * than the specified object.
     */
    @Test
    public void shouldBeNegativeIfThisIsSmaller() {
        T smaller = createSmallerSut();
        T greater = createGreaterSut();

        assertThat(smaller.compareTo(greater)).isNegative();
        assertThat(greater.compareTo(smaller)).isPositive();

        assertThat(smaller.compareTo(smaller)).isZero();
        assertThat(greater.compareTo(greater)).isZero();
    }

    /**
     * Verifies that {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))} for all {@code x} and {@code y}.
     */
    @Test
    public void shouldBeSymmetric() {
        T left = createSmallerSut();
        T right = createGreaterSut();

        int leftCompareToResult = left.compareTo(right);
        int rightCompareToResult = right.compareTo(left);

        assertThat(Integer.signum(leftCompareToResult)).isEqualTo(-Integer.signum(rightCompareToResult));
    }

    /**
     * Creates a subject under test. The SUT must be smaller than the SUT of the opposite method {@link
     * #createGreaterSut()}.
     *
     * @return the SUT
     */
    protected abstract T createSmallerSut();

    /**
     * Creates a subject under test. The SUT must be greater than the SUT of the opposite method {@link
     * #createSmallerSut()}.
     *
     * @return the SUT
     */
    protected abstract T createGreaterSut();
}
```

## Typische Anwendungsgebiete des Abstract Test Patterns

Neben solchen API Tests wird das Pattern hauptsächlich genutzt, um für den Code von abstrakten Klassen auch Testfälle 
zur Verfügung zu stellen. Diese Testfälle können dann von Subklassen einfach mitbenutzt werden. So kann sicher
gestellt werden, dass Subklassen den Vertrag einer Vererbungshierarchie nicht brechen und damit nicht das 
[Liskov Substitution Principle](http://www.objectmentor.com/resources/articles/lsp.pdf) verletzen.