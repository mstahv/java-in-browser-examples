package org.vaadin.example;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.firitin.appframework.MenuItem;
import org.vaadin.firitin.components.RichText;

@MenuItem(title = "About", order = 0)
@Route(layout = Layout.class)
public class MainView extends VerticalLayout {

    public MainView() {
        add(new RichText().withMarkDown("""
        ## Vaadin Flow extensions in pure Java
        
        Vaadin Flow provides a 100% Java solution for building web applications.
        It's unique architecture allows you to build web UIs in true Java with 
        the real JVM on the server side. This means you can use all Java libraries
        and frameworks, and also your existing Java skills.
        
        But, in case you need to customize things further on the browser, this
        usually means also some JS. In the past Vaadin actually used GWT for the 
        components too, but today the default is JS and Web Components. But Java
        in the browser is still there if you need it. GWT is still maintained 
        (and used by some Vaadin Flow users), and there are also newer options
        to choose from.
               
        As an example, this project provides two Tetris game implementations. It
        re-uses the same game core that has worked for use for years as [a WebSocket 
        demo](https://tetris.demo.vaadin.com). But here, the core game (graphics 
        and controls) is executed in the browser, not on the server the rest of the
        application. Examples are:
        
        * CheerpJ - a commercial product by Leaning Technologies that can run 
          unmodified Java bytecode in the browser. It has an actual JVM that runs
          in the browser's sandbox in WebAssembly. The Vaadin component executes
           the raw Java Swing game with CheerpJ and sends game state events to the 
           Vaadin application (running in a separate regular JVM on the server), 
           which shows your current score in the info card built with Vaadin while 
           you smash those full rows! 
           Starting a JVM with Swing app (in the browser!!) naturally takes a bit 
           of time, but it is surprisingly efficient on a modern browser and good
           network connection. CheerpJ is free for non-commercial use. 
           [More info, cheerpj.com](https://cheerpj.com).
        * TeaVM is more "GWT like" solution that emits JavaScript or WebAssembly 
          from Java bytecode (GWT transpiles from Java -> JavaScript). 
          Like GWT, it doesn't have a JVM, so no reflection etc, but like GWT apps, 
          it starts like regular JS apps and it's open source and free to use. The 
          Vaadin component packages the game implementation compiled with TeaVM and 
          sends game state events to the Vaadin application running on the server. 
          [More info, teavm.org](https://teavm.org).
        
        **ATTENTION!** Both this kind of solutions break the "server-side security model"
        of Vaadin Flow that you might be very accustomed to. In this kind of architecture 
        you cannot trust anything coming from the browser, so in this kind of architecture 
        you should validate input on the server side as well, especially if you can't trust
        the users. This is not a problem for this demo, but e.g. if  the game results would 
        be used for some true rewards, you should probably fall back to a more [traditional 
        Vaadin UI implementation](https://tetris.demo.vaadin.com) to avoid cheating.
        
        Full source code of the examples is available in the [GitHub repository](https://github.com/mstahv/java-in-browser-examples).
        
        An arcticle about these examples is coming soon...
        
        """));
    }

}
