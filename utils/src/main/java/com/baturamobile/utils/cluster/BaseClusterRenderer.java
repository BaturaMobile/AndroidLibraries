package com.baturamobile.utils.cluster;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.ViewGroup;

import com.baturamobile.data.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

/**
 * Created by vssnake on 04/09/2017.
 */

public abstract class BaseClusterRenderer<T extends BaseClusterItem>  extends DefaultClusterRenderer<T> {

    private Context mContext;

    private final float mDensity;

    private final IconGenerator mIconClusterGenerator;

    public BaseClusterRenderer(Context context, com.google.android.gms.maps.GoogleMap map, ClusterManager<T> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
        mDensity = context.getResources().getDisplayMetrics().density;
        mIconClusterGenerator = new IconGenerator( context );
        mIconClusterGenerator.setStyle(IconGenerator.STYLE_DEFAULT);
        mIconClusterGenerator.setContentView( makeSquareTextView( context, 6 ) );
    }


    @Override
    protected void onBeforeClusterItemRendered( T item, MarkerOptions markerOptions )
    {
        markerOptions.icon( BitmapDescriptorFactory.fromBitmap( item.getCachedBitmap() ) );
        markerOptions.alpha(item.alpha);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<T> cluster, MarkerOptions markerOptions )
    {
        int clusterSize = getBucket( cluster );
        for ( T Item : cluster.getItems() )
        {
            if ( Item.isEnabled() )
            {
                mIconClusterGenerator.setBackground( makeClusterBackground( getColor() ) );
                BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap( mIconClusterGenerator.makeIcon( getClusterText( clusterSize ) ) );
                markerOptions.icon( descriptor );
                return;
            }
        }
        mIconClusterGenerator.setBackground( makeClusterBackground( getColor() ) );

        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap( mIconClusterGenerator.makeIcon( getClusterText( clusterSize ) ) );
        markerOptions.icon( descriptor );

    }


    private LayerDrawable makeClusterBackground(int color )
    {
        ShapeDrawable mColoredCircleBackground = new ShapeDrawable( new OvalShape() );
        mColoredCircleBackground.getPaint().setColor( color );
        ShapeDrawable outline = new ShapeDrawable( new OvalShape() );
        outline.getPaint().setColor( 0x80ffffff );
        LayerDrawable background = new LayerDrawable( new Drawable[]
                {
                        outline, mColoredCircleBackground
                } );
        int strokeWidth = ( int ) ( mDensity * 3.0F );
        background.setLayerInset( 1, strokeWidth, strokeWidth, strokeWidth, strokeWidth );
        return background;
    }

    private SquareTextView makeSquareTextView(Context context, int padding )
    {
        SquareTextView squareTextView = new SquareTextView( context );
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT );
        squareTextView.setLayoutParams( layoutParams );
        squareTextView.setId( R.id.amu_text );
        int paddingDpi = ( int ) ( padding * mDensity );
        squareTextView.setPadding( paddingDpi, paddingDpi, paddingDpi, paddingDpi );
        squareTextView.setTextColor( Color.WHITE );
        squareTextView.setTypeface( Typeface.DEFAULT_BOLD );
        return squareTextView;
    }

    protected abstract int getColor();
}
