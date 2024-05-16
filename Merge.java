import java.util.*;
class ImageMerge {
    public IntervalST<Double, double[]> IST = new IntervalST<>();
    public double[][] BBs;
    public double threshold;
    
    public double IOU(double[] box1 ,double[] box2){
        // x1, y1, w, h
        double overlap = (Math.min(box1[0]+box1[2] , box2[0]+box2[2]) - Math.max(box1[0], box2[0])) * (Math.min(box1[1]+box1[3] , box2[1]+box2[3]) - Math.max(box1[1], box2[1]));
        double union = (box1[2]*box1[3]) + (box2[2]*box2[3]) - overlap;
        return overlap/union;
    }
    public boolean box_intersect(double y1, double y2, double y11, double y22){
        if((y1 >= y11 && y2 <= y11) || ( y11 >= y1 && y22 <= y1) ) {
            return true;
        }
        return false;
    }
    public double[] new_merge_box(double[] box1, double[] box2){
        double x1 = Math.min(box1[0], box2[0]);
        double w =  Math.max(box1[0]+box1[2], box2[0]+box2[2] ) - x1;
        double y1 = Math.max(box1[1],box2[1]);
        double h =  Math.max(box1[1]+box1[3], box2[1]+box2[3]) - y1;
        double[] ans = {x1,y1,w,h};
        return ans;
    }
    public boolean the_same_box(double[] box1, double[] box2){
        if(box1[0]==box2[0] && box1[1]==box2[1] && box1[2]==box2[2] && box1[3]==box2[3]){
            return true;
        }
        return false;
    }
    public double[][] mergeBox()
    {
        //return merged bounding boxes just as input in the format of 
        //[up_left_x,up_left_y,width,height]

        List<double[]> answer = new ArrayList<>();
        for(double[] box : BBs){
            double start = box[0];
            double end = box[0] + box[2];
            List<double[]> interval_nodes = IST.intersects(start,end); // return array of interval nodes
            for( double[] interval_node : interval_nodes){
                if (the_same_box(interval_node, box)) {
                    IST.delete(interval_node[0], interval_node[0]+interval_node[2]);
                    continue;
                }
                if( box_intersect(interval_node[1], interval_node[1]+interval_node[3], box[1], box[1]+box[3]) ) {
                    if (IOU(interval_node, box) > threshold) {
                        answer.add(new_merge_box(interval_node, box));
                    }
                }
            }

        }
        // change List<double[]> to double[][]
        double[][] final_answer = new double[ answer.size() ][4];
        int index = 0;
        for (double[] box : answer) {
            final_answer[index][0] = box[0];
            final_answer[index][1] = box[1];
            final_answer[index][2] = box[2];
            final_answer[index][3] = box[3];
        }
        return final_answer;
    }
    public ImageMerge(double[][] bbs, double iou_thresh){
        //bbs(bounding boxes): [up_left_x,up_left_y,width,height]
        //iou_threshold:          [0.0,1.0]
        threshold = iou_thresh;
        // new tree
        for (double[] box : bbs) {
            IST.put(box[0], box[0]+box[2], box);
        }
    }
    // public static void draw(double[][] bbs)
    // {
    //     // ** NO NEED TO MODIFY THIS FUNCTION, WE WON'T CALL THIS **
    //     // ** DEBUG ONLY, USE THIS FUNCTION TO DRAW THE BOX OUT** 
    //     StdDraw.setCanvasSize(960,540);
    //     for(double[] box : bbs)
    //     {
    //         double half_width = (box[2]/2.0);
    //         double half_height = (box[3]/2.0);
    //         double center_x = box[0]+ half_width;
    //         double center_y = box[1] + half_height;
    //         //StdDraw use y = 0 at the bottom, 1-center_y to flip
    //         StdDraw.rectangle(center_x, 1-center_y, half_width,half_height);
    //     }
    // }
    public static void main(String[] args) {
        ImageMerge sol = new ImageMerge(
                new double[][]{
                        {0.02,0.01,0.1,0.05},{0.0,0.0,0.1,0.05},{0.04,0.02,0.1,0.05},{0.06,0.03,0.1,0.05},{0.08,0.04,0.1,0.05},
                        {0.24,0.01,0.1,0.05},{0.20,0.0,0.1,0.05},{0.28,0.02,0.1,0.05},{0.32,0.03,0.1,0.05},{0.36,0.04,0.1,0.05},
                },
                0.5
        );
        double[][] temp = sol.mergeBox();
        // ImageMerge.draw(temp);
    } 
}