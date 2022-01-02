package de.vagtsi.examples.guicejavamodule.app.dfs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.vagtsi.examples.guicejavamodule.api.PluginModule;

/**
 * One node within the dependency tree (used for dependency resolution).
 */
public class DependencyNode {
  private static final Logger logger = LoggerFactory.getLogger(DependencyNode.class);

  // the plugin module this node represents
  private final PluginModule plugin;

  // our dependencies
  private HashMap<String, DependencyNode> edges = new HashMap<>();

  // reference to owning graph for creating or retrieving sub nodes
  private final DependencyGraph graph;

  public DependencyNode(DependencyGraph graph, PluginModule plugin) {
    this.graph = graph;
    this.plugin = plugin;
  }

  public String getId() {
    return plugin.id();
  }
  
  public PluginModule getPlugin() {
	return plugin;
  }

  public Collection<DependencyNode> getEdges() {
    return edges.values();
  }

  /**
   * Add dependencies (edges) to this node. Skips/ignores dependencies having no node.
   * 
   * @param dependencies dependency list of this (plugin) node
   */
  public void createDependencies() {
    for (String dependencyId : plugin.parentIds()) {
      if (!this.getId().equals(dependencyId) && !edges.containsKey(dependencyId)) {
        DependencyNode node = graph.getNode(dependencyId);
        if (node != null) {
          edges.put(dependencyId, node);
        }
      }
    }
  }

  /**
   * Adds a node dependency directly to this node without any further type check
   * 
   * @param node the node this node is dependent on
   */
  public void add(DependencyNode node) {
    // prevent for 'cyclic dependencies' through self references
    if (node != this) {
      edges.put(node.getId(), node);
    }
  }

  /**
   * Resolve this nodes dependencies via depth-first search (recursively!)
   * 
   * @throws Exception if detected circular dependency
   */
  public void resolve(Collection<DependencyNode> resolved, Set<DependencyNode> unresolved) {
    unresolved.add(this); // remember this node for detecting circular dependencies
    for (DependencyNode edge : getEdges()) {
      if (!resolved.contains(edge)) {
        if (unresolved.contains(edge)) {
          String msg = String.format("Circular dependency detected: %s -> %s", getId(), edge.getId());
          logger.error(msg);
          throw new IllegalArgumentException(msg);
        }

        edge.resolve(resolved, unresolved);
      }
    }
    resolved.add(this);
    unresolved.remove(this);
  }

  /**
   * Print the node/pluginId to e.g. logging
   */
  @Override
  public String toString() {
    return getId();
  }
}
