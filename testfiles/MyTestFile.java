package testfiles;

public class MyClass {
    public void hello() {
	for (int i = 0; i < 3; i++) {
	     System.out.println(i);
	}
        System.out.println("Hello World");
    }
    public static void main(String[] args) {
	MyClass myClass = new MyClass();
	myClass.hello();
    }
}
