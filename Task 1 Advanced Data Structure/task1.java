import java.util.ArrayList;

class PowerOfTwoMaxHeap {
    private ArrayList<Integer> heap;
    private int numChildren;

    // constructor - x is the number of children per parent node
    public PowerOfTwoMaxHeap(int x) {
        this.heap = new ArrayList<>();
        this.numChildren = (int) Math.pow(2, x);
    }

    // inserts new element into heap
    public void insert(int val) {
        heap.add(val);
        heapifyUp(heap.size() - 1); // bubble up the newly added element
    }

    public int pop() {
        // check if heap is empty
        if (heap.size() == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        // retreives max value of heap and removes the smallest value
        int maxVal = heap.get(0);
        int lastVal = heap.remove(heap.size() - 1);

        if (heap.size() > 0) {
            heap.set(0, lastVal); // swap the smallest to root
            heapifyDown(0); // bubble down the first element
        }

        return maxVal;
    }

    // bubbles up value in idx
    public void heapifyUp(int idx) {
        int parentIdx = (idx - 1) / numChildren;
        while (idx > 0 && heap.get(idx) > heap.get(parentIdx)) {
            // swap parent and cur index
            int temp = heap.get(idx);
            heap.set(idx, heap.get(parentIdx));
            heap.set(parentIdx, temp);

            // update parentIdx and idx
            idx = parentIdx;
            parentIdx = (idx - 1) / numChildren;
        }
    }

    // bubbles down value in idx
    public void heapifyDown(int idx) {
        int largest = idx;
        while (true) {
            int childIdx;

            // go through all children to find if anyone is larger
            for (int i = 1; i <= numChildren; i++) {
                childIdx = numChildren * idx + i;
                if (childIdx < heap.size() && heap.get(childIdx) > heap.get(largest)) {
                    largest = childIdx;
                }
            }

            // if the largest we found was not idx, we swap down
            if (largest != idx) {
                // swap largest and cur index
                int temp = heap.get(idx);
                heap.set(idx, heap.get(largest));
                heap.set(largest, temp);

                // update idx
                idx = largest;
            } else {
                break; // in right order
            }
        }
    }
}