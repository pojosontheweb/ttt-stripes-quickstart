> NOTE : TTT is still a prototype project and should not yet be used in production systems.
It's still in beta and could have bugs and/or limitations. 

# Going static-typed with Stripes and TTT

Many solutions exist for templating pages in Java Webapps.
The main one is JSP, which is supported natively by the JEE
containers, and alternatives also exist for those who don't 
like JSP (Freemarker, Velocity, 
Groovy, ...).

The concept is usually the same : the template mixes text and code
in order to render the view.

When using a MVC framework, a typical request handling flow goes like this :

1. the Controller handles the request, and prepares the data to be rendered
2. the View handles the rendering of the data
 
With most of the Java MVCs, the link between the Controller and the View is 
"loose-typed" : the Controller is not linked to the View at compile-time, 
and the View gets its data from a loose-typed, map-style structure. There 
is a gap between Controller and View, that the compiler cannot help to fill.

This makes for bug-prone code, as you cannot detect errors at compile-time, 
and instead get runtime exceptions.

## JSP : The Loose-typed Way

Let's illustrate the problem, using Stripes, and plain JSP.
 
Here's the action bean (`MyAction.java`) :

```java
public class MyAction implements ActionBean {

	// get/setContext
	
	@DefaultHandler
	public Resolution display() {
		// forward to a JSP
		return new ForwardResolution("/WEB-INF/my.jsp");	
	}

	// 'foo' property, that we'll display in the view
	public String getFoo() {
		...
	}

}
```

Doesn't look bad, if you're used to it. But it actually is.

The Action's event handler blindly forwards to a resource, without 
any guarantee that it will be there. Also, there is no way to know 
what kind of data the template expects.

Now, let's have a look at the View (`/WEB-INF/my.jsp`) :

```jsp
<%@ page ... %>
<html>
...
	<div class="my">
		Hello, ${actionBean.foo} !
	</div>
</html>
```

Again, for somebody used to JSP, this looks pretty good. But again, it ain't.

The JSP blindly evaluates the EL expression `${actionBean.foo}`, meaning that :

1. it tries to look on a scope (page, request, session, app) if there is an attribute
named `actionBean`. You have absolutely no guarantee that it'll be there at runtime. 
2. then is looks for a `getFoo()` method on the `actionBean` object. Again, you have 
absolutely no guarantee that there will be such a property on the object, assuming it's there 
in the first place. You also have no knowledge of the actual type of this `foo` property. 

Again, this late-bound technique makes for error-prone code, and doesn't leverage the compiler in order to check 
for errors at compile-time. You really are at the mercy of a typo here. It's quite 
sad, considering you are using a strong-typed language (Java) everywhere else.

Couldn't we have Views linked to Controllers at compile time, and let the good old compiler do
what it's good at ?
 
## Going statically-typed with TTT
 
TTT is a framework for creating strong-typed text templates. It basically compiles 
your templates to Java classes that expose the template's signature, like a function. 
Rendering the template is just like calling the function, passing it the appropriate 
arguments. 

With TTT, code rendering the template is statically bound to the template, and 
the template can only use the arguments that it declares. 

> TTT is similar to (and actually inspired from) Scala Templates in Play2, but in pure Java. 

The TTT syntax is a subset of JSP : you basically mix 
text and Java code (expressions or scriptlets). There are major differences 
though :

* the templates are compiled to Java classes at build-time
* the templates define their signature via a declaration block (betwen `<%!` and `%>`)
* you cannot use "real" JSP features like EL or tag libraries (at least in XML, we'll see that later)
 
TTT is integrated to Stripes, so you can use all of the neat Stripes features (like 
the Stripes taglib), with the benefits of static typing and compile-time error checking.

### Typed Views

Let's rewrite our example JSP as a TTT tmplate (`MyTemplate.ttt`) :
 
```jsp
<%!
	String foo;
%>
<html>
...
	<div class="my">
		Hello, <%= foo %> !
	</div>
</html>
```

The template first defines its signature, in a directive, between `<%!` and `%>`.
This block contains the arguments required by the template. 
Here we only define a `String foo` argument. 

Then, the `foo` argument is used in the template, in a scriptlet, between `<%=` and 
`%>`. The value returned by the expression is rendered in the template.

As you see, it really looks like a JSP, but it's not. 

The template is compiled (at design/build time) to an immutable Java class that has :

* a unique constructor that accepts the arguments as defined in the template's signature
* a `render(Writer out)` method that renders the template to the passed writer

Now, how do we use this from an ActionBean ?

### Template Resolutions

The TTT/Stripes integration provides a `TttResolution` class that handles the 
rendering of the templates. It can be used directly from Stripes ActionBeans.

Here is a rewritten version of our previous example :

```java
public class MyAction implements ActionBean {

	// get/setContext
	
	@DefaultHandler
	public Resolution display() {
		// create the template using required args
		MyTemplate myTemplate = new MyTemplate(getFoo());
		// return a resolution to render it
		return new TttResolution(myTemplate);
	}

	// 'foo' property, that we'll display in the view
	public String getFoo() {
		...
	}

}
```

Instead of blindly forwarding to a JSP, the ActionBean is now linked  
to the View at compile-time. It doesn't need to set any request attribute 
or anything, it's plain Java.

### Using the Stripes Tags

Stripes has a fantastic JSP Tag Library that you can use to create links, FORMs, 
and many other goodies. Unfortunately TTT is pure Java and doesn't allow for 
classic JSP tags. 

The TTT/Stripes integration provides a pure Java version of the Stripes taglib. 
It provides a way to reuse arbitrary JSP tags in TTT, and provides a fluent API
over the Stripes tags. 

Let's use a `stripes:url` to add a hyperlink in our example (`MyTemplate.ttt`) :

```jsp
<%!
	String foo;
%>
<%
	// get Stripes "tag lib"
	StripesTags stripes = new StripesTags(out);
%>
<html>
...
	<div class="my">
		Hello, <%= foo %> ! 
		<a href="<%= stripes.url(MyOtherAction.class) %>">
			a link
		</a>
	</div>
</html>
```

Pretty easy. We simply get an instance of `StripesTags`, and use methods on that 
object that correspond to the tag we need. 

Some more involved Stripes Tags accept a body, like `stripes:form`. Those 
are used with try/resource blocks, in scriptlets. 

Let's create another template with a Stripes FORM (`MyFormTemplate.ttt`) :

```jsp
<%!
	User user;
%>
<%
	// get Stripes "tag lib"
	StripesTags stripes = new StripesTags(out);
%>
<html>

...

<h1>
	<% if (user == null) { %>
		You are not authenticated, please 
		<% try ( Link l = stripes.link(MyLoginAction.class) ) { %>
			sign-in
		<% } %>
	<% } else { %>
		Hello, <%= user.getUsername() %> - 
		<% try ( Link l = stripes.link(MyLogoutAction.class) ) { %>
			disconnect		
		<% } %>
	<% } %>
</h1>

<%-- open a FORM for MyOtherAction --%>
<% try ( Form f = stripes.form(MyOtherAction.class).set("class", "my-form") ) { %>
		
	...
		
	<div class="my-form-group">
	
		<%-- localized s:label --%>
		<%= stripes.label("type") %>
		
		<%-- 
			s:text input : FORM inputs are obtained by calling methods on
			the Form instance instead of the taglib's entry point
			because you never have a stripes:text outside of a stripes:form...
		--%>
		<%= f.text("myProp") %>
	
	</div>
	
	<div class="my-form-controls">
	
		<%-- s:submit buttons --%>
		<%= f.submit("doStuff").set("class", "btn-primary") %>
		<%= f.submit("doOtherStuff") %>
		 	
	</div>
	
	...

<%-- end of s:form --%>
<% } %>
	
...

</html>
```

As you see, you use the `StripesTags` class as an entry-point to the tags. The only thing to 
pay attention to is how you use the tags, particularly try/resource vs body-less tags).

### Page Templating (layouts)

The Stripes taglib provides layout tags (`stripes:layout`) that allows to create
templates for pages or page fragments. Those are used to create the global page 
layouts that can be reused in views. 

When using TTT, you don't need those tags. They are not provided by `StripesTags` because
TTT has built-in composition/nesting capabilities.

Let's say we want a global template to reuse in various pages, with an arbitrary content. 

Here's how we'd write the main, outer template (`MyPageTemplate.ttt`) :

```jsp
<%!
	/**
	 * the title of the page
	 */
	String pageTitle;
	
	/**
	 * the template to be used as a body
	 */
	ITemplate body;
%>
<html>
<head>
	<title><%= pageTitle %></title>
	<link rel="stylesheet" ...>
	...
<head>
<body>
	<%-- render the body here --%>
	<%= body %>
</body>
</html>
```

Our `MyPageTemplate` accepts two arguments :

* `String pageTitle` a title for the page
* `ITemplate body` the body of the page

The `body` argument is a TTT template itself (`ITemplate`), therefore, it gets 
rendered when you use it inside an expression block, like `<%= body %>`.
 
We can now create full-blown pages by passing an arbitrary template to 
our global `MyPageTemplate` :

```java
MyTemplate body = new MyTemplate(foo);
MyPageTemplate htmlPage = new MyPageTemplate("my page", body);
return new TttResolution(htmlPage);
```

## Conclusion

Using TTT instead of JSP provides static-typed Views, bridging the gap between Controllers 
and Views in Stripes. It allows to write cleaner code, and find bugs earlier. 

The TTT/Stripes integration allows to write and render templates easily, with a 
very low entry level and fast learning curve. Since it is all plain Java, there 
ain't no magic involved and you code and invoke Views just like you'd invoke 
any other Java object.

TTT ships with a CLI compiler, and a maven plugin for compiling the templates at build-time.
Compilation of the templates is pretty much transparent.

If you're using IntelliJ IDEA, the `ttt-idea` can compile 
your templates on the fly, without invoking the external compiler. This improves 
productivity a lot as you don't have to use any external tool.


