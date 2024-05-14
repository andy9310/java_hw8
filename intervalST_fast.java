import java.util.*;
class IntervalST<Key extends Comparable<Key>, Value>{
    private Node root;

    private class Node {
        private Key lo, hi, max;
        private Value val;
        private int size;
        private Node left, right;
        
        public Node(Key lo, Key hi, Value val) {
            // initializes the node if required.
            this.lo = lo;
            this.hi = hi;
            this.val = val;
            this.max = this.hi;
        }
    }

    public IntervalST()
    {
        // initializes the tree if required.
    }
    
    public void put(Key lo, Key hi, Value val)
    {
         root = putting(root, lo, hi, val);
         // check_right_node(root);
        // insert a new interval here.
        // lo    : the starting point of the interval. lo included
        // hi    : the ending point of the interval. hi included
        // val   : the value stored in the tree.
    }
    private Node putting(Node x, Key lo, Key hi, Value val) {
        if (x == null) return new Node(lo, hi, val);
        int cmp = lo.compareTo(x.lo);
        if (cmp < 0) x.left = putting(x.left, lo, hi, val);
        else if (cmp > 0) x.right = putting(x.right, lo, hi, val);
        else{
            // compare ending point
            int end_cmp = hi.compareTo(x.hi);
            if(end_cmp < 0) x.left = putting(x.left, lo, hi, val);
            else if (end_cmp > 0) x.right = putting(x.right, lo, hi, val);
            else{
                x.val = val;
            }
        }
        x.max = max3(x.hi, max(x.left), max(x.right));
        if(x.max == null){
            System.out.println("wrong");
        }
        
        return x;
    }
    private Key max(Key a, Key b) {
        if (a == null) return b;
        if (b == null) return a;
        return (a.compareTo(b) > 0) ? a : b;
    }
    ////
    private Key max(Node x) {
        if (x == null) return null;
        if (x.right == null) return x.hi;
        else return max(x.right);
    }

    private Key max3(Key a, Key b, Key c) {
        return max(max(a, b), c);
    }


    public void delete(Key lo, Key hi)
    {
        root = deleting(root, lo, hi);
        // check_right_node(root);
        // remove an interval of [lo,hi]
        // do nothing if interval not found.
    }
    private Node deleting(Node x, Key lo, Key hi) {
        if (x == null) return null;

        int cmp = lo.compareTo(x.lo);
        if (cmp < 0) x.left = deleting(x.left, lo, hi);
        else if (cmp > 0) x.right = deleting(x.right, lo, hi);
        else{
            int end_cmp = hi.compareTo(x.hi);
            if(end_cmp < 0) x.left = deleting(x.left, lo, hi);
            else if (end_cmp > 0) x.right = deleting(x.right, lo, hi);
            else{
                if (x.right == null) return x.left;
                if (x.left == null) return x.right;
                Node t = x;
                x = min(t.right);
                x.right = deleteMin(t.right);
                x.left = t.left;
            }  
        }

        x.max = max3(x.hi, max(x.left), max(x.right));
        if(x.max == null){
            System.out.println("delete wrong");
        }
        return x;
    }
    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }
    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.max = max3(x.hi, max(x.left), max(x.right));
        if(x.max == null){
            System.out.println("deleteMin wrong");
        }
        return x;
    }

    public List<Value> intersects(Key lo, Key hi)
    {
        List<Value> result = new ArrayList<>();
        intersects(root, lo, hi, result);
        return result;
        // return the values of all intervals within the tree which intersect with [lo,hi].
    }
    private void intersects(Node x, Key lo, Key hi, List<Value> result) {
        if (x == null){
            System.out.println("null");
            return;
        }
        if (intersects(x.lo, x.hi, lo, hi)) result.add(x.val);

        if (x.left != null && x.left.max.compareTo(lo) >= 0)
            intersects(x.left, lo, hi, result);
        if (x.right != null && x.right.max.compareTo(lo) >= 0){
            intersects(x.right, lo, hi, result);
        }
            
    }
    private boolean intersects(Key lo1, Key hi1, Key lo2, Key hi2) {
        return lo1.compareTo(hi2) <= 0 && hi1.compareTo(lo2) >= 0;
    }
    private void check_right_node(Node x){
        if(x == null) return;
        System.out.println(x.val);
        check_right_node(x.right);
    }










    public static void main(String[]args)
    {
        // Example
        IntervalST<Integer, String> IST = new IntervalST<>();
        IST.put(2,5,"badminton");
        IST.put(1,5,"PDSA HW7");
        IST.put(3,5,"Lunch");
        IST.put(3,6,"Workout");
        IST.put(3,7,"Do nothing");
        IST.delete(2,5); // delete "badminton"
        System.out.println(IST.intersects(1,2));
        
        IST.put(8,8,"Dinner");
        System.out.println(IST.intersects(6,10));
        
        IST.put(3,7,"Do something"); // If an interval is identical to an existing node, then the value of that node is updated accordingly
        System.out.println(IST.intersects(7,7));
        
        IST.delete(3,7); // delete "Do something"
        System.out.println(IST.intersects(7,7));
    }
}