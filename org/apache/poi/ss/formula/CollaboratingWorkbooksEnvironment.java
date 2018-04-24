package org.apache.poi.ss.formula;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.util.Internal;

@Internal
public final class CollaboratingWorkbooksEnvironment {
    public static final CollaboratingWorkbooksEnvironment EMPTY = new CollaboratingWorkbooksEnvironment();
    private final WorkbookEvaluator[] _evaluators;
    private final Map<String, WorkbookEvaluator> _evaluatorsByName;
    private boolean _unhooked;

    private CollaboratingWorkbooksEnvironment() {
        this._evaluatorsByName = Collections.emptyMap();
        this._evaluators = new WorkbookEvaluator[0];
    }

    public static void setup(String[] workbookNames, WorkbookEvaluator[] evaluators) {
        int nItems = workbookNames.length;
        if (evaluators.length != nItems) {
            throw new IllegalArgumentException("Number of workbook names is " + nItems + " but number of evaluators is " + evaluators.length);
        } else if (nItems < 1) {
            throw new IllegalArgumentException("Must provide at least one collaborating worbook");
        } else {
            CollaboratingWorkbooksEnvironment collaboratingWorkbooksEnvironment = new CollaboratingWorkbooksEnvironment(workbookNames, evaluators, nItems);
        }
    }

    public static void setup(Map<String, WorkbookEvaluator> evaluatorsByName) {
        if (evaluatorsByName.size() < 1) {
            throw new IllegalArgumentException("Must provide at least one collaborating worbook");
        }
        CollaboratingWorkbooksEnvironment collaboratingWorkbooksEnvironment = new CollaboratingWorkbooksEnvironment(evaluatorsByName, (WorkbookEvaluator[]) evaluatorsByName.values().toArray(new WorkbookEvaluator[evaluatorsByName.size()]));
    }

    public static void setupFormulaEvaluator(Map<String, FormulaEvaluator> evaluators) {
        Map<String, WorkbookEvaluator> evaluatorsByName = new HashMap(evaluators.size());
        for (Entry<String, FormulaEvaluator> swb : evaluators.entrySet()) {
            String wbName = (String) swb.getKey();
            FormulaEvaluator eval = (FormulaEvaluator) swb.getValue();
            if (eval instanceof WorkbookEvaluatorProvider) {
                evaluatorsByName.put(wbName, ((WorkbookEvaluatorProvider) eval)._getWorkbookEvaluator());
            } else {
                throw new IllegalArgumentException("Formula Evaluator " + eval + " provides no WorkbookEvaluator access");
            }
        }
        setup(evaluatorsByName);
    }

    private CollaboratingWorkbooksEnvironment(String[] workbookNames, WorkbookEvaluator[] evaluators, int nItems) {
        this(toUniqueMap(workbookNames, evaluators, nItems), evaluators);
    }

    private static Map<String, WorkbookEvaluator> toUniqueMap(String[] workbookNames, WorkbookEvaluator[] evaluators, int nItems) {
        Map<String, WorkbookEvaluator> evaluatorsByName = new HashMap((nItems * 3) / 2);
        for (int i = 0; i < nItems; i++) {
            String wbName = workbookNames[i];
            WorkbookEvaluator wbEval = evaluators[i];
            if (evaluatorsByName.containsKey(wbName)) {
                throw new IllegalArgumentException("Duplicate workbook name '" + wbName + "'");
            }
            evaluatorsByName.put(wbName, wbEval);
        }
        return evaluatorsByName;
    }

    private CollaboratingWorkbooksEnvironment(Map<String, WorkbookEvaluator> evaluatorsByName, WorkbookEvaluator[] evaluators) {
        IdentityHashMap<WorkbookEvaluator, String> uniqueEvals = new IdentityHashMap(evaluators.length);
        for (Entry<String, WorkbookEvaluator> me : evaluatorsByName.entrySet()) {
            String uniEval = (String) uniqueEvals.put(me.getValue(), me.getKey());
            if (uniEval != null) {
                throw new IllegalArgumentException("Attempted to register same workbook under names '" + uniEval + "' and '" + ((String) me.getKey()) + "'");
            }
        }
        unhookOldEnvironments(evaluators);
        hookNewEnvironment(evaluators, this);
        this._unhooked = false;
        this._evaluators = (WorkbookEvaluator[]) evaluators.clone();
        this._evaluatorsByName = evaluatorsByName;
    }

    private static void hookNewEnvironment(WorkbookEvaluator[] evaluators, CollaboratingWorkbooksEnvironment env) {
        int i;
        IEvaluationListener evalListener = evaluators[0].getEvaluationListener();
        for (WorkbookEvaluator evaluationListener : evaluators) {
            if (evalListener != evaluationListener.getEvaluationListener()) {
                throw new RuntimeException("Workbook evaluators must all have the same evaluation listener");
            }
        }
        EvaluationCache cache = new EvaluationCache(evalListener);
        for (i = 0; i < nItems; i++) {
            evaluators[i].attachToEnvironment(env, cache, i);
        }
    }

    private void unhookOldEnvironments(WorkbookEvaluator[] evaluators) {
        Set<CollaboratingWorkbooksEnvironment> oldEnvs = new HashSet();
        for (WorkbookEvaluator environment : evaluators) {
            oldEnvs.add(environment.getEnvironment());
        }
        CollaboratingWorkbooksEnvironment[] oldCWEs = new CollaboratingWorkbooksEnvironment[oldEnvs.size()];
        oldEnvs.toArray(oldCWEs);
        for (CollaboratingWorkbooksEnvironment unhook : oldCWEs) {
            unhook.unhook();
        }
    }

    private void unhook() {
        if (this._evaluators.length >= 1) {
            for (WorkbookEvaluator detachFromEnvironment : this._evaluators) {
                detachFromEnvironment.detachFromEnvironment();
            }
            this._unhooked = true;
        }
    }

    public WorkbookEvaluator getWorkbookEvaluator(String workbookName) throws WorkbookNotFoundException {
        if (this._unhooked) {
            throw new IllegalStateException("This environment has been unhooked");
        }
        WorkbookEvaluator result = (WorkbookEvaluator) this._evaluatorsByName.get(workbookName);
        if (result != null) {
            return result;
        }
        StringBuffer sb = new StringBuffer(256);
        sb.append("Could not resolve external workbook name '").append(workbookName).append("'.");
        if (this._evaluators.length < 1) {
            sb.append(" Workbook environment has not been set up.");
        } else {
            sb.append(" The following workbook names are valid: (");
            int count = 0;
            for (String append : this._evaluatorsByName.keySet()) {
                int count2 = count + 1;
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append("'").append(append).append("'");
                count = count2;
            }
            sb.append(")");
        }
        throw new WorkbookNotFoundException(sb.toString());
    }
}
