public class PRQuadTree {
    static final int QT_NODE_CAPACITY = 1;
    int N; // The tree represents an n x n region.
    Node root;
    //This class represents a rectangle area.
    static class Rectangle {
        Coordinates center; // Coordinates of the center
        int height, width; // Height is the distance from the center to the top boundary and Width from center to the side boundary.
        public Rectangle(Coordinates center, int height, int width){
            this.center = center;
            this.height = height;
            this.width = width;
        }
    }
    //This is the node-class of the quad tree. Represents a box of a two-dimensional space.
    static class Node {
        int depth;
        Coordinates[] keys;
        Rectangle region;
        Node northWest;
        Node northEast;
        Node southWest;
        Node southEast;
        public Node(Coordinates[] keys, int depth, Rectangle region){
            this.depth = depth;
            this.keys = keys;
            northWest = northEast = southWest = southEast = null;
            this.region = region;
        }
    
        public Node(int depth, Rectangle region){
            this.depth = depth;
            this.keys = new Coordinates[QT_NODE_CAPACITY];
            northWest = northEast = southWest = southEast = null;
            this.region = region;
        }
        // Method for checking if point is within the region of the current node. If the point is on the intersection line (border) of two nodes,
        // belongs to the left or bottom node.
        private boolean containsPoint(Coordinates point){
            long x = region.center.getX();
            long y = region.center.getY();
            return point.getX() >= (x - region.width) && point.getX() < (x + region.width) && point.getY() >= (y - region.height) && point.getY() < (y + region.height);
        }

        private int numberOfKeysContaining(){
            int counter =0;
            for (int i=0; i<QT_NODE_CAPACITY; i++){
                if (keys != null){
                    if (keys[i] != null) counter++;
                }
            }
            return counter;
        }
    }
    // Constructors.
    public PRQuadTree(int n){
        this.N =n;
        this.root = new Node(0, new Rectangle(new Coordinates(N/2, N/2), N/2, N/2));
    }
    public PRQuadTree(int n, Node root){
        this.N =n;
        this.root = root;
    }


    // Method to subdivide a node.
    private void subdivide(Node node){
        if (node.northEast == null) {// if one child is null, all of them are null.
            // Defining the rectangles for the subdivided regions.
            long x = node.region.center.getX();
            long y = node.region.center.getY();
            Rectangle nw = new Rectangle(new Coordinates((x - node.region.width/2), (y + node.region.height/2)), node.region.height/2, node.region.width/2);
            Rectangle ne = new Rectangle(new Coordinates((x + node.region.width/2), (y + node.region.height/2)), node.region.height/2, node.region.width/2);
            Rectangle sw = new Rectangle(new Coordinates((x - node.region.width/2), (y - node.region.height/2)), node.region.height/2, node.region.width/2);
            Rectangle se = new Rectangle(new Coordinates((x + node.region.width/2), (y - node.region.height/2)), node.region.height/2, node.region.width/2);

            node.northWest = new Node(node.depth + 1, nw);
            node.northEast = new Node(node.depth + 1, ne);
            node.southWest = new Node(node.depth + 1, sw);
            node.southEast = new Node(node.depth + 1, se);
            for (int i=0; i<QT_NODE_CAPACITY; i++){ // Copy all keys to the new leafs.
                Coordinates key = node.keys[i];
                node.keys[i] = null;// Set current node's keys to null. Only leafs containing keys!
                Node s = insertHelp(node, key);
            }
        }
    }

    public Node search(Coordinates key){return searchHelp(root, key);}

    private Node searchHelp(Node root, Coordinates key){
       if (!root.containsPoint(key)) {return null;}

       if (root.southEast==null){
           for (Coordinates k: root.keys){
               if (k!=null && key.getX() == k.getX() && key.getY() == k.getY()) {MultiCounter.increaseCounter(2, root.depth);return root;}
           }
           MultiCounter.increaseCounter(2, root.depth);
           return null; // If key doesn't exist.
       }

        if (root.northWest!=null && root.northWest.containsPoint(key)) return searchHelp(root.northWest, key);
        if (root.northEast!=null && root.northEast.containsPoint(key)) return searchHelp(root.northEast, key);
        if (root.southWest!=null && root.southWest.containsPoint(key)) return searchHelp(root.southWest, key);
        if (root.southEast!=null && root.southEast.containsPoint(key)) return searchHelp(root.southEast, key);


        // Should never reach here.
        return null;
    }
    public Node insert(Coordinates key) {return insertHelp(root, key);}
    private Node insertHelp(Node root, Coordinates key) {
        for (int i=0; i<root.numberOfKeysContaining(); i++)//Check if key exists.
            if (root.keys[i] == key) {return null;}
        // Ignore keys that are out of region.
        if (!root.containsPoint(key)) {return null;}
            // if there is space, and it's a leaf node add the key.
        else if ((root.numberOfKeysContaining() < QT_NODE_CAPACITY && root.northWest == null)) {
            //System.out.println(".");
            root.keys[root.numberOfKeysContaining()] = key;
            return root;
        }
        else if (root.northEast == null) subdivide(root);
        Node search;
        search= insertHelp(root.northEast, key);
        if (search != null) {return search;}
        search = insertHelp(root.northWest, key);
        if (search != null) {return search;}
        search = insertHelp(root.southEast, key);
        if (search != null) {return search;}
        search = insertHelp(root.southWest, key);
        if (search != null) {return search;}

        // Otherwise, the point cannot be inserted for some unknown reason (this should never happen).
        return null;
    }
    
}
