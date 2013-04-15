package taohu

class ScalaHelloWorld {
  def scalaSayHello() {
    println("Scala says Hello, world!")
  }

  def test() {
    val javaHelloWorld = new JavaHelloWorld
    javaHelloWorld javaSayHello()
  }
}