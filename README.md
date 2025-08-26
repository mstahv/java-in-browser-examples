# Demo: The Full Java Experience Expanded to the Browser



## Modules in this example:

This aggregation module builds these:


 * **tetris-lib**, this is a generic Java library, contains shared parts of components
 * **plain-java-tetris**, this module creates a very basic Java Swing app, that you can launch locally with basic JVM if you want
 * **cheerpj-tetris-addon**, this Vaadin add-on executes the *plain-jav-tetris* using a JVM, running in the browser, powered by CheerpJ. To demonstrate data exchange with the CheerpJ process and Vaadin component (running in a traditional JVM in the browser) the current score and game state is emitted as events.
 * **teavm-tetris-addon**, this Vaadin add-on executes the *tetris-lib* using TeaVM and renders the gamestate to HTML5 canvas using TeaVMs JS interoperability APIs. The produced JavaScript (could be transpiled also to WebAssembly) runs in the browser without any JVM and is thus way faster to load, pretty much like raw JS. Naturally missing then a lot of powers of JVM. The packaged add-on contains the transpiled JavaScript code, and a Vaadin component that uses it. Game state and score is emitted as events to the Vaadin component (running in a traditional JVM in the browser).
 * **gwt-tetris-addon** is **missing!!** This one doesn't need myth busting, this is how standard Vaadin components were built for years. It is still certainly possible as well.
 * **demo-app** is a Vaadin application that uses all the above components to demonstrate them.

Suggested testing setup, make a priming build and then run the demo-app:

```
mvn install
java -jar demo-app/target/*jar
````

(Execute DevModeApplication class from demo-app/src/test/java/ if running via IDE)

Example is also deployed online: TODO
