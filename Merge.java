
import com.sun.source.tree.Tree;

class ImageMerge {
    public IntervalSearchTree IST;
    public double[][] BBs;
    public double threshold;
    
    public double IOU(){

    }
    public boolean box_intersect(double y1, double y2, double y11, double y22){
        if((y1 >= y11 && y2 <= y11) || ( y11 >= y1 && y22 <= y1) ) {
            return true;
        }
        return false;
    }
    public double[][] mergeBox()
    {
        //return merged bounding boxes just as input in the format of 
        //[up_left_x,up_left_y,width,height]

        double[][] answer = new double[BBs.length][BBs[0].length];
        int index = 0;
        for(double[] box : BBs){
            double start = box[0];
            double end = box[0] + box[2];
            double[][] interval_nodes = IST.intersect(start,end); // return array of interval nodes
            for( double[] interval_node : interval_nodes){
                if(box_intersect(interval_node.val[0],interval_node.val[1],box[1],box[1]-box[3])) {
                    if (IOU() > threshold) {
                        answer[index][0] = min(start, interval_node.min);
                        answer[index][1] =  max(end,interval_node.max) - answer[index][0];
                        answer[index][2] = max(box[1],interval_node.val[0]);
                        answer[index][3] = answer[index][2] - min(box[1]-box[3],interval_node.val[1]);
                    }
                }
            }

        }
        return answer;
    }
    public ImageMerge(double[][] bbs, double iou_thresh){
        //bbs(bounding boxes): [up_left_x,up_left_y,width,height]
        //iou_threshold:          [0.0,1.0]
        threshold = iou_thresh;
        // new tree
        IST = new IntervalSearchTree();
        for (double[] box : bbs) {
            IST.insert(box[0], box[3]);
        }
    }
    public static void draw(double[][] bbs)
    {
        // ** NO NEED TO MODIFY THIS FUNCTION, WE WON'T CALL THIS **
        // ** DEBUG ONLY, USE THIS FUNCTION TO DRAW THE BOX OUT** 
        StdDraw.setCanvasSize(960,540);
        for(double[] box : bbs)
        {
            double half_width = (box[2]/2.0);
            double half_height = (box[3]/2.0);
            double center_x = box[0]+ half_width;
            double center_y = box[1] + half_height;
            //StdDraw use y = 0 at the bottom, 1-center_y to flip
            StdDraw.rectangle(center_x, 1-center_y, half_width,half_height);
        }
    }
    public static void main(String[] args) {
        ImageMerge sol = new ImageMerge(
                new double[][]{
                        {0.02,0.01,0.1,0.05},{0.0,0.0,0.1,0.05},{0.04,0.02,0.1,0.05},{0.06,0.03,0.1,0.05},{0.08,0.04,0.1,0.05},
                        {0.24,0.01,0.1,0.05},{0.20,0.0,0.1,0.05},{0.28,0.02,0.1,0.05},{0.32,0.03,0.1,0.05},{0.36,0.04,0.1,0.05},
                },
                0.5
        );
        double[][] temp = sol.mergeBox();
        ImageMerge.draw(temp);
    } 
}