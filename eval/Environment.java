package eval;

import java.util.HashMap;

/**
 * A referencing environment for bindings
 */
public class Environment {

    private HashMap<String, Double> bindings;

    public Environment() {
        bindings = new HashMap<>();
    }

    public double put(String var, double val) {
        bindings.put(var, val);
        return val;
    }

    public double get(int pos, String var) throws EvalException {
        if (!bindings.containsKey(var)) {
            throw new EvalException(pos, "Undefined variable: " + var);
        }
        return bindings.get(var);
    }
}
