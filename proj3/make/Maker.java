package make;

import graph.DepthFirstTraversal;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

import static java.util.Arrays.asList;
import static make.Main.error;

/**
 * Represents a makefile.
 *
 * @author P. N. Hilfinger
 */
class Maker {

    /**
     * Describes Makefile lines that are ignored.
     */
    static final Pattern IGNORED = Pattern.compile("\\s*(#.*)?");
    /**
     * Describes a rule header in a makefile:  TARGET: DEPENDENCIES.
     */
    static final Pattern HEADER =
            Pattern.compile("([^:\\s]+)\\s*:\\s*(.*?)\\s*");
    /**
     * Describes a sequence of valid targets and whitespace.
     */
    static final Pattern TARGETS = Pattern.compile("[^:=#\\\\]*$");
    /**
     * Describes an indented command line.
     */
    static final Pattern COMMAND = Pattern.compile("(\\s+.*)");
    /**
     * Describes the separator on dependencies lines.
     */
    static final Pattern SPACES = Pattern.compile("\\p{Blank}+");

    /**
     * Read and store the ages of existing targets from the
     * file named FILEINFONAME.
     */
    void readFileAges(String fileInfoName) {
        String name = "<unknown>";
        String target;
        int age;

        try {
            FileReader read = new FileReader(fileInfoName);
            Scanner inp = new Scanner(read);
            _currentTime = inp.nextInt();
            while (inp.hasNext()) {
                target = inp.next();
                age = inp.nextInt();
                _ages.put(target, age);
            }
            inp.close();
        } catch (FileNotFoundException excp) {
            System.err.printf("Error could not find makefile: %s",
                    fileInfoName);

        } catch (NoSuchElementException excp) {
            System.err.printf("Error near entry for %s: %s", name,
                    excp.getMessage());
        }
    }

    /**
     * Read make rules from the file named MAKEFILENAME and form the dependence
     * graph with targets as vertices.
     */
    void readMakefile(String makefileName) {
        Scanner inp;
        String target;
        ArrayList<String> dependencies;
        ArrayList<String> commands;

        target = null;
        dependencies = null;
        commands = null;
        try {
            inp = new Scanner(new FileReader((makefileName)));

        } catch (FileNotFoundException excp) {
            error("Error could not find makefile: %s", makefileName);
            return;
        }

        while (inp.hasNextLine()) {
            String line = inp.nextLine();
            Matcher parsed;
            parsed = IGNORED.matcher(line);
            if (parsed.matches()) {
                continue;
            }
            parsed = HEADER.matcher(line);
            if (parsed.matches()) {
                addRule(target, dependencies, commands);
                target = parsed.group(1);
                if (!TARGETS.matcher(target).matches()) {
                    error("Error bad target: '%s'", target);
                }
                if (!TARGETS.matcher(parsed.group(2)).matches()) {
                    error("Error one or more bad prerequisites: '%s'",
                            parsed.group(2));
                }
                dependencies = new ArrayList<>();
                if (!parsed.group(2).isEmpty()) {
                    dependencies.addAll(asList(SPACES.split(parsed.group(2))));
                }
                commands = new ArrayList<>();
                continue;
            }
            parsed = COMMAND.matcher(line);
            if (target != null && parsed.matches()) {
                commands.add(parsed.group(1));
            } else {
                error("Error erroneous input line: '%s'", line);
            }
        }
        addRule(target, dependencies, commands);
    }

    /**
     * Add rule
     * TARGET: DEPENDENCIES
     * COMMANDS
     * to makegraph, or add DEPENDENCIES and COMMANDS to that rule, if it
     * already exists.  Returns the rule.
     */
    private Rule addRule(String target,
                         List<String> dependencies,
                         List<String> commands) {
        if (target != null) {
            Rule rule;
            rule = _targets.get(target);
            if (rule == null) {
                rule = new Rule(this, target);
                _targets.put(target, rule);
            }
            for (String dependency : dependencies) {
                Rule depRule = addRule(dependency,
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList());
                rule.addDependency(depRule);
            }
            rule.addCommands(commands);
            return rule;
        } else {
            return null;
        }
    }

    /**
     * Issue instructions to build TARGET.
     */
    void build(String target) {
        Rule targetRule = addRule(target, Collections.<String>emptyList(),
                Collections.<String>emptyList());
        int v = targetRule.getVertex();
        if (_traversal == null) {
            _traversal = new MakeTraversal();
            _traversal.traverse(v);
        } else {
            _traversal.traverse(v);
        }
    }

    /**
     * Return my dependence graph.
     */
    final Depends getGraph() {
        return _depends;
    }

    /**
     * Return the initial age of TARGET, if it exists, or null if it
     * does not.
     */
    final Integer getInitialAge(String target) {
        return _ages.get(target);
    }

    /**
     * Returns the current time (to be attached to rebuilt targets).
     */
    final int getCurrentTime() {
        return _currentTime;
    }

    /**
     * The current time. Should be no earlier than the time on the
     * latest file.
     */
    private int _currentTime;
    /**
     * The makefile dependency graph.
     */
    private Depends _depends = new Depends();
    /**
     * Mapping of target names to their ages.
     */
    private HashMap<String, Integer> _ages = new HashMap<>();
    /**
     * Mapping of target names to their Rules.
     */
    private HashMap<String, Rule> _targets = new HashMap<>();
    /**
     * Depth-first traversal of my vertices.
     */
    private MakeTraversal _traversal;

    /**
     * Traversal for make dependency graph.
     */
    class MakeTraversal extends DepthFirstTraversal {
        /**
         * A traversal of my dependency graph.
         */
        MakeTraversal() {
            super(_depends);
        }

        /**
         * Revisit vertex V after traversing its successors. Returns false iff
         * the traversal is to terminate immediately.
         */
        @Override
        protected boolean postVisit(int v0) {
            _depends.getLabel(v0).rebuild();
            return true;
        }
        @Override
        protected void processSuccessors(int u) {
            for (int v : _depends.successors(u)) {
                if (processSuccessor(u, v)) {
                    _fringe.add(v);
                }
            }
        }
        @Override
        protected boolean shouldPostVisit(int v) {
            return true;
        }
    }
}
