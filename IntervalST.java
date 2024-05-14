class IntervalSearchTree {
    class Node {
        int lo, hi, maxHi;
        Node left, right;
        
        Node(int lo, int hi) {
            this.lo = lo;
            this.hi = hi;
            this.maxHi = hi;
        }
    }
    
    Node root;
    
    public void insert(int lo, int hi) {
        Node node = root;
        Node parent = null;
        
        while (node != null) {
            parent = node;
            if (lo < node.lo) {
                node = node.left;
            } else {
                node = node.right;
            }
            
            if (parent.maxHi < hi) {
                parent.maxHi = hi;
            }
        }
        
        if (parent == null) {
            root = new Node(lo, hi);
        } else if (lo < parent.lo) {
            parent.left = new Node(lo, hi);
        } else {
            parent.right = new Node(lo, hi);
        }
    }
    
    public int[] intersect(int lo, int hi) {
        Node node = root;
        
        while (node != null) {
            if (node.hi < lo || hi < node.lo) {
                if (node.left != null && node.left.maxHi >= lo) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            } else {
                return new int[]{node.lo, node.hi};
            }
        }
        
        return null;
    }
}