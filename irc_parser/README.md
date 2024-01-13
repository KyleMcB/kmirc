This probably is a terrible IRC parser.
I should do some more reading on parsing.
anyways the thing I really wanted to mention here was the test module patter
there is a header_test module which provides an abstract base class for tests
this means the test suite is decoupled from the actually implementation.
Definitely more of a pain, but I think it will come in handy latter.

## The pattern

1. write a suspending function that is the impl of the test. Use getTestSubject to stay agnostic of the impl.
2. write an abstract function of roughly the same name, so that any classes that implement this base class will have to
   implement the abstract fun.
3. In the leaf class, just have all the abstract funs point to the suspending functions of the same name. mark
   with `@Test` and use `= runTest {`

I love the idea of decoupled tests, so I took a swing at a way to write them with KMP, but its a little ugly. However, I
could write a large amount of tests cases and maybe use a totally 