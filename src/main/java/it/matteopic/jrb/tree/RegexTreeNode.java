package it.matteopic.jrb.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import net.amygdalum.regexparser.AlternativesNode;
import net.amygdalum.regexparser.BoundedLoopNode;
import net.amygdalum.regexparser.CharClassNode;
import net.amygdalum.regexparser.ConcatNode;
import net.amygdalum.regexparser.DefinedCharNode;
import net.amygdalum.regexparser.GroupNode;
import net.amygdalum.regexparser.OptionalNode;
import net.amygdalum.regexparser.RegexNode;
import net.amygdalum.regexparser.UnboundedLoopNode;

public class RegexTreeNode extends DefaultMutableTreeNode {

	public RegexTreeNode(RegexNode rn){
		setUserObject(rn);
		
		if(rn instanceof AlternativesNode){
			AlternativesNode an = (AlternativesNode)rn;
			for(RegexNode sn : an.getSubNodes()){
				add(new RegexTreeNode(sn));
			}
		}
		else if(rn instanceof ConcatNode){
			ConcatNode an = (ConcatNode)rn;
			for(RegexNode sn : an.getSubNodes()){
				add(new RegexTreeNode(sn));
			}
		}else if(rn instanceof GroupNode){
			GroupNode an = (GroupNode)rn;
			add(new RegexTreeNode(an.getSubNode()));
			
		}else if(rn instanceof UnboundedLoopNode){
			UnboundedLoopNode an = (UnboundedLoopNode)rn;
			add(new RegexTreeNode(an.getSubNode()));
		}
		else if(rn instanceof BoundedLoopNode){
			BoundedLoopNode an = (BoundedLoopNode)rn;
			add(new RegexTreeNode(an.getSubNode()));
		}
		else if(rn instanceof OptionalNode){
			OptionalNode an = (OptionalNode)rn;
			add(new RegexTreeNode(an.getSubNode()));
		}
		else if(rn instanceof CharClassNode){
			CharClassNode an = (CharClassNode)rn;
			for(DefinedCharNode cn : an.toCharNodes()){
				add(new RegexTreeNode(cn));				
			}
		}
	}

	public RegexNode getRegexNode(){
		return (RegexNode)getUserObject();
	}
}
