package aleksander73.cheems.utility.functional_interface;

public interface BiFunction<I, O> {
    public abstract O accept(I input1, I input2);
}
