package de.vagtsi.examples.guicejavamodule.app.dfs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import de.vagtsi.examples.guicejavamodule.api.PluginModule;

/**
 * Class representing a dependency graph of plugins
 * using the <a href="http://en.wikipedia.org/wiki/Depth-first_search">Depth-first search algorithm</a>.
 */
public class DependencyGraph {
	/** the map of all nodes (dependent plugins) */
	private HashMap<String, DependencyNode> nodes = new HashMap<>();

	/**
	 * Create a new graph on all plugins retrieved with given ServiceLoader. 
	 */
	public static DependencyGraph create(List<PluginModule> pluginModules) {
	  DependencyGraph graph = new DependencyGraph();
	  graph.createNodes(pluginModules);
	  graph.createDependencyGraph();
	  return graph;
	}
	/**
	 * Create a node for each <bplugin</b>.
	 */
	private void createNodes(List<PluginModule> plugins) {
		for (PluginModule plugin : plugins) {
				//create a node for this plugin 
				//-> will create additional nodes for all feature dependencies as well! 
				String pluginId = plugin.id();
				if (!nodes.containsKey(pluginId)) {
					nodes.put(
							pluginId, 
							new DependencyNode(this, plugin));
				}
		}
	}

	/**
	 * Create the 'real' dependency graph (all edges) for existing nodes
	 * by the defined/parsed feature dependencies.  
	 */
	public void createDependencyGraph() {
		for (DependencyNode node : getNodes()) {
			node.createDependencies();
		}
	}
	
	/**
	 * Create the list of resolved dependencies via depth-first search algorithm:
	 * find all node/packages having no dependencies at first
	 * @return list of resolved dependencies, least dependent node (kar file) first  
	 */
	public Collection<DependencyNode> resolve() {
		ArrayList<DependencyNode> resolved = new ArrayList<>(getNodeCount());
		HashSet<DependencyNode> unresolved = new HashSet<>();
		for (DependencyNode node : getNodes()) {
			if (!resolved.contains(node)) {
				node.resolve(resolved, unresolved);
			}
		}
		return resolved;
	}
	
	/**
	 * Retrieves existing (plugin) node identified by the pluginId
	 * from the graph.
	 * @param pluginId the name of the feature to retrieve it's node
	 * @return instance of a node identified by the pluginId or null if such a node does not exist
	 */
	public DependencyNode getNode(String pluginId) {
		return nodes.get(pluginId);
	}

	public int getNodeCount() {
		return nodes.size();
	}
	
	public Collection<DependencyNode> getNodes() {
		return nodes.values();
	}
}
