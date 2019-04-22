public class test {
    public static void main(String[] args){
        A a1 = new A();
        A a2 = new A(a1);

        a1.b = "qweqeqweq";

        System.out.println(a1.b);
        System.out.println(a2.b);
    }
}


class A{
//    B b;
    String b = "asd";
    A(){}
    A(A a){this.b = a.b;}
}

class B{
    int c = 20;
}