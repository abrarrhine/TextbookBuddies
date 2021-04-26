package com.example.textbookbuddies.search;
import android.content.Context;

import com.example.textbookbuddies.R;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Utilities for Restaurants.
 */
public class ListingUtil {

    private static final String TAG = "ListingUtil";

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(2, 4, 60,
            TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private static final String RESTAURANT_URL_FMT = "https://storage.googleapis.com/firestorequickstarts.appspot.com/food_%d.png";

    private static final int MAX_IMAGE_NUM = 22;

    private static final String[] TITLE = {
            "Foo",
            "Bar",
            "Baz",
    };

    /**
     * Create a random Restaurant POJO.
     */
    public static Listing getRandom(Context context) {
        Listing listing = new Listing();
        Random random = new Random();

        // Categories (first element is 'Any')
        String[] categories = context.getResources().getStringArray(R.array.categories);
        categories = Arrays.copyOfRange(categories, 1, categories.length);

        String[] locations = context.getResources().getStringArray(R.array.locations);
        locations = Arrays.copyOfRange(locations, 1, locations.length);

        int[] prices = new int[]{1, 2, 3};

        listing.setTitle(getRandomTitle(random));
        listing.setIsbn("XXXXXXXXXXXXXX");
        listing.setSubject(getRandomString(categories, random));
        listing.setPhoto(getRandomImageUrl(random));
        listing.setPrice(1 + (100 - 1) * random.nextDouble());
        listing.setLocation(getRandomString(locations, random));
        listing.setContact("clairetaylor@vt.edu");

        return listing;
    }

    private static String getRandomImageUrl(Random random) {
        // Integer between 1 and MAX_IMAGE_NUM (inclusive)
        int id = random.nextInt(MAX_IMAGE_NUM) + 1;

        return String.format(Locale.getDefault(), RESTAURANT_URL_FMT, id);
    }

    public static String getPriceString(double price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(price);
    }

    private static String getRandomTitle(Random random) {
        return getRandomString(TITLE, random);
    }

    private static String getRandomString(String[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }

}