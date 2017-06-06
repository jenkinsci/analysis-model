package lecture;

public class Quiz {

    public static void main(final String[] args) {

        smallReferences();
        System.out.println("-------------");
        largeReferences();
        System.out.println("-------------");
        equals();
    }
    public static void smallReferences() {
        int intValue = 15;
        int otherIntValue = 15;
        Integer literal = 15;
        Integer valueOf = Integer.valueOf(15);
        Integer integer = new Integer(15);

        System.out.println(intValue == otherIntValue);
        System.out.println(intValue == integer);
        System.out.println(integer == literal);
        System.out.println(literal == valueOf);
    }

    public static void largeReferences() {
        int intValue = 1500;
        int otherIntValue = 1500;
        Integer literal = 1500;
        Integer valueOf = Integer.valueOf(1500);
        Integer integer = new Integer(1500);

        System.out.println(intValue == otherIntValue);
        System.out.println(intValue == integer);
        System.out.println(integer == literal);
        System.out.println(literal == valueOf);
    }

    public static void equals() {
        int intValue = 15;
        Integer literal = 15;
        Integer valueOf = Integer.valueOf(15);
        Integer integer = new Integer(15);

        System.out.println(literal.equals(intValue));
        System.out.println(literal.equals(valueOf));
        System.out.println(literal.equals(integer));
        System.out.println(integer.equals(intValue));
        System.out.println(valueOf.equals(literal));


    }
}
