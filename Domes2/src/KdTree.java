public class KdTree {

    /*
    Nested class representing Nodes.
    * */
    class Node {
        Coordinates value;
        Node left, right;

        public Node(Coordinates value) {
            this.value = value;
            left = right = null;
        }
    }
    //Root of the tree.
    Node root;
    //Constructors
    public KdTree() {root = null;}
    public KdTree(Coordinates value){ root = new Node(value); }

    //This method is a simplification of insertHelp. Just calls the insertHelp method.
    void insert(Coordinates key){root = insertHelp(this.root, key, 0);}
    //This method is a simplification of searchHelp. Just calls the searchHelp method.
    Node search(Coordinates key){return searchHelp(this.root, key, 0);}

    /*
    A method to insert a new key (x,y) to the KdTree.
    It uses the recursive approach.
     */
    private  Node insertHelp(Node root,Coordinates key, int currentDepth){
        if (root == null){
            root = new Node(key);
            return root;
        }
        //if current depth is even number, compare x. Else compare y.
        else if (currentDepth%2 == 0){
            if (key.getX() < root.value.getX()){root.left = insertHelp(root.left, key, currentDepth + 1);}
            else {root.right = insertHelp(root.right, key, currentDepth + 1);}
            }
        else {
            if (key.getY() < root.value.getY()){root.left = insertHelp(root.left, key, currentDepth + 1);}
            else {root.right = insertHelp(root.right, key, currentDepth + 1);}
        }
        // return the (unchanged) node pointer
        return root;
    }

    /*
    A method to search a specific key (x,y) in the k-d-tree.
    Using the recursive approach.
     */
   private Node searchHelp(Node root, Coordinates key, int currentDepth) {
        if (root == null || (key.getX() == root.value.getX() && key.getY() == root.value.getY())) {
            MultiCounter.increaseCounter(1, currentDepth); //counter[0]+=current depth. For complexity analysis of the algorithm.
            return root;
        }
        else if (currentDepth%2 == 0) {
            if (key.getX() < root.value.getX()) return searchHelp(root.left, key, currentDepth + 1);
            else return searchHelp(root.right, key, currentDepth + 1);
        }
        else {
            if (key.getY() < root.value.getY()) return searchHelp(root.left, key, currentDepth +1);
            else return searchHelp(root.right, key, currentDepth +1);
        }

    }


}
