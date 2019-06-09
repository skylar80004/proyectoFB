package com.example.proyectofb;

import java.util.ArrayList;

public class QuickSort {

    ArrayList<Post> postList;



   public QuickSort(ArrayList postList){
       this.postList = postList;
   }

    /* This function takes last element as pivot,
        places the pivot element at its correct
        position in sorted array, and places all
        smaller (smaller than pivot) to left of
        pivot and all greater elements to right
        of pivot */
    int partition( int low, int high)
    {

        String pivotString = this.postList.get(high).getTotalTime();
        int pivot = Integer.parseInt(pivotString);

        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than or
            // equal to pivot
            String jString = this.postList.get(j).getTotalTime();
            int jValue = Integer.parseInt(jString);

            if ( jValue <= pivot)
            {
                i++;

                // swap arr[i] and arr[j]


                Post temp = this.postList.get(i);
                this.postList.set(i,this.postList.get(j));
                this.postList.set(j,temp);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        Post temp = this.postList.get(i+1);
        //int temp = arr[i+1];
        this.postList.set(i+1,this.postList.get(high));
        this.postList.set(high,temp);

        return i+1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    void sort( int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(low, high);

            // Recursively sort elements before
            // partition and after partition
            sort( low, pi-1);
            sort( pi+1, high);
        }
    }

    /* A utility function to print array of size n */
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i=0; i<n; ++i)
            System.out.print(arr[i]+" ");
        System.out.println();
    }


}
