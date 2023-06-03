/**
 * Class representing a lined list.
 *
 * @author Thomas Lagkalis
 * @param <T> the type of the first key in each node of linked list
 * @param <U> the type of the second key in each node of linked list
 */
public class LinkedList <T, U>{
    private final Node root;

    public LinkedList(){
        this.root = null;
    }

    public LinkedList(T key1, U key2){
        this.root = new Node(null, null, key1, key2);
    }


    /**
     * This class represents node in a linked list.
     * First node prev=null.
     * Last node next=null.
     */
    private class Node {
        Node prev;
        Node next;
        T key1;
        U key2;

        //Constructor
        public Node(Node prev, Node next, T key1, U key2) {
            this.prev = prev;
            this.next = next;
            this.key1 = key1;
            this.key2 = key2;
        }
    }

        /**
         *
         * @param node (usually root)
         * @return the last node of the list
         */
        private Node findLastNode(Node node){
            if (node.next == null) return node;
            else return findLastNode(node.next);
        }

        /**
         * Inserts a node with new information in the linked list.
         *
         * @param key1 the first key
         * @param key2 the second key
         */
        public void insert(T key1, U key2){
            if (root != null) {
                Node lastNode = findLastNode(root);
                lastNode.next = new Node(lastNode, null, key1, key2);
            }
        }

        /**
         *
         * @return a string representation of the linked list or null if root is null.
         */
        public String toString(){if (root!=null) return toStringHelp(new StringBuilder(),root); else return null;}

        private String toStringHelp(StringBuilder s, Node nd){
            if (nd.next != null){ //Not the last node.
                s.append(nd.key1 + " " + nd.key2 + "\n");
                return toStringHelp(s, nd.next);
            }
            else{ //reached the last node.
                s.append(nd.key1 + " " + nd.key2 + "\n");
                return s.toString();
            }
        }
}
