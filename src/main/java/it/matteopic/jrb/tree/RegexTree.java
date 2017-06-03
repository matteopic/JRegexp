package it.matteopic.jrb.tree;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;

import net.amygdalum.regexparser.RegexNode;

public class RegexTree extends JTree {
	
	public RegexTree(RegexNode rn){
		setModel(new DefaultTreeModel( new RegexTreeNode(rn) ));
		setCellRenderer(new Renderer());
		setRowHeight(20);
		setRootVisible(false);
		setShowsRootHandles(true);
	}

	public void set(RegexNode node){
		setModel(new DefaultTreeModel( new RegexTreeNode(node) ));
	}
}
