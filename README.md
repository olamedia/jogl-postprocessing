jogl-postprocessing
===================

Filters and blends using shaders with JOGL's FBO

```java
{
  /* GLAutoDrawable drawable */
  final BaseGLEventListener listener = new BaseGLEventListener();
  // BaseGLEventListener holds instance of filter chain object
  final FilterChainRenderer filterChain = listener.getFilterChain();
  // Add filters
  final Sepia sepia = new Sepia();
  filterChain.addFilter(sepia);
  // Add scene
  listener.setSceneRenderer(/* IRenderer */ sceneRenderer);
  // Set filter chain renderer as primary GLEventListener
  drawable.addGLEventListener(listener);
}

```

* Some things was not implemented probably ^_^ - i have tested only a limited set of filters
