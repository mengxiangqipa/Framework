package com.luck.picture.lib.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.R;
import com.luck.picture.lib.anim.OptAnimationLoader;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.luck.picture.lib.tools.StringUtils;
import com.luck.picture.lib.tools.ToastManage;
import com.luck.picture.lib.tools.VoiceUtils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * author：luck
 * project：PictureSelector
 * package：com.luck.picture.lib.adapter
 * email：893855882@qq.com
 * data：2016/12/30
 */
public class PictureImageGridAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int DURATION = 450;
    private Context context;
    private boolean showCamera = true;
    private OnPhotoSelectChangedListener imageSelectChangedListener;
    private int maxSelectNum;
    private List<LocalMedia> images = new ArrayList<LocalMedia>();
    private List<LocalMedia> selectImages = new ArrayList<LocalMedia>();
    private boolean enablePreview;
    private int selectMode = PictureConfig.MULTIPLE;
    private boolean enablePreviewVideo = false;
    private boolean enablePreviewAudio = false;
    private boolean is_checked_num;
    private boolean enableVoice;
    private int overrideWidth, overrideHeight;
    private float sizeMultiplier;
    private Animation animation;
    private PictureSelectionConfig config;
    private int mimeType;
    private boolean zoomAnim;
    // 所有文件最大限制
    private long totalFileMaxLenth;
    // 单个文件最大限制
    private long singleFileMaxLenth;
    /**
     * 单选图片
     */
    private boolean isGo;

    public PictureImageGridAdapter(Context context, PictureSelectionConfig config) {
        this.context = context;
        this.config = config;
        this.selectMode = config.selectionMode;
        this.showCamera = config.isCamera;
        this.maxSelectNum = config.maxSelectNum;
        this.enablePreview = config.enablePreview;
        this.enablePreviewVideo = config.enPreviewVideo;
        this.enablePreviewAudio = config.enablePreviewAudio;
        this.is_checked_num = config.checkNumMode;
        this.overrideWidth = config.overrideWidth;
        this.overrideHeight = config.overrideHeight;
        this.enableVoice = config.openClickSound;
        this.sizeMultiplier = config.sizeMultiplier;
        this.mimeType = config.mimeType;
        this.zoomAnim = config.zoomAnim;
        this.totalFileMaxLenth = config.totalFileMaxLenth;
        this.singleFileMaxLenth = config.singleFileMaxLenth;
        animation = OptAnimationLoader.loadAnimation(context, R.anim.modal_in);
    }

    public void setShowCamera(boolean showCamera) {
        this.showCamera = showCamera;
    }

    public void bindImagesData(List<LocalMedia> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void bindSelectImages(List<LocalMedia> images) {
        // 这里重新构构造一个新集合，不然会产生已选集合一变，结果集合也会添加的问题
        List<LocalMedia> selection = new ArrayList<>();
        for (LocalMedia media : images) {
            selection.add(media);
        }
        this.selectImages = selection;
        subSelectPosition();
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectImages);
        }
    }

    public List<LocalMedia> getSelectedImages() {
        if (selectImages == null) {
            selectImages = new ArrayList<>();
        }
        return selectImages;
    }

    public List<LocalMedia> getImages() {
        if (images == null) {
            images = new ArrayList<>();
        }
        return images;
    }

    @Override
    public int getItemViewType(int position) {
        if (showCamera && position == 0) {
            return PictureConfig.TYPE_CAMERA;
        } else {
            return PictureConfig.TYPE_PICTURE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PictureConfig.TYPE_CAMERA) {
            View view = LayoutInflater.from(context).inflate(R.layout.picture_item_camera, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.picture_image_grid_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == PictureConfig.TYPE_CAMERA) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*以下判断个数，类型*/
                    if (selectImages.size() >= maxSelectNum) {
                        boolean eqImg = false;
                        String str = eqImg ? context.getString(R.string.picture_message_max_num, maxSelectNum + "")
                                : context.getString(R.string.picture_message_video_max_num, maxSelectNum + "");
                        ToastManage.s(context, str);
                        return;
                    }
                    int videoCnt = 0;
                    for (int i = 0, len = selectImages.size(); i < len; i++) {
                        LocalMedia localMedia = selectImages.get(i);
                        if (PictureMimeType.isPictureType(localMedia.getPictureType()) == PictureMimeType.ofVideo()) {
                            if (++videoCnt >= config.maxVideoNum) {
                                String str = context.getString(R.string.picture_message_video_max_count,
                                        config.maxVideoNum);
                                ToastManage.s(context, str);
                                return;
                            }
                        }
                    }
                    if (config.onlyOneMimeType) {
                        if (selectImages != null && selectImages.size() > 0) {
                            LocalMedia localMedia = selectImages.get(0);
                            if (null != localMedia) {
                                if ((PictureMimeType.isPictureType(localMedia.getPictureType()) == PictureMimeType.ofVideo())) {
                                    if (config.maxVideoNum >= 1) {
                                        String tip = String.format(Locale.getDefault(), "只能选择至多%d个视频",
                                                config.maxVideoNum);
                                        ToastManage.s(context, tip);
                                        return;
                                    }
                                } else if ((PictureMimeType.isPictureType(localMedia.getPictureType()) == PictureMimeType.ofImage())) {
                                    if (config.maxVideoNum >= 1) {
                                        String tip = String.format(Locale.getDefault(), "只能选择至多%d张图片",
                                                config.maxSelectNum);
                                        ToastManage.s(context, tip);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                    /*以上判断个数，类型*/
                    if (imageSelectChangedListener != null) {
                        imageSelectChangedListener.onTakePhoto();
                    }
                }
            });
        } else {
            final ViewHolder contentHolder = (ViewHolder) holder;
            final LocalMedia image = images.get(showCamera ? position - 1 : position);
            image.position = contentHolder.getAdapterPosition();
            final String path = image.getPath();
            final String pictureType = image.getPictureType();
            if (is_checked_num) {
                notifyCheckChanged(contentHolder, image);
            }
            selectImage(contentHolder, isSelected(image), false);

            final int mediaMimeType = PictureMimeType.isPictureType(pictureType);
            boolean gif = PictureMimeType.isGif(pictureType);

            contentHolder.tv_isGif.setVisibility(gif ? View.VISIBLE : View.GONE);
            if (mimeType == PictureMimeType.ofAudio()) {
                contentHolder.tv_duration.setVisibility(View.VISIBLE);
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.picture_audio);
                StringUtils.modifyTextViewDrawable(contentHolder.tv_duration, drawable, 0);
            } else {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.video_icon);
                StringUtils.modifyTextViewDrawable(contentHolder.tv_duration, drawable, 0);
                contentHolder.tv_duration.setVisibility(mediaMimeType == PictureConfig.TYPE_VIDEO
                        ? View.VISIBLE : View.GONE);
            }
            boolean eqLongImg = PictureMimeType.isLongImg(image);
            contentHolder.tv_long_chart.setVisibility(eqLongImg ? View.VISIBLE : View.GONE);
            long duration = image.getDuration();
            contentHolder.tv_duration.setText(DateUtils.timeParse(duration));
            if (mimeType == PictureMimeType.ofAudio()) {
                contentHolder.iv_picture.setImageResource(R.drawable.audio_placeholder);
            } else {
                RequestOptions options = new RequestOptions();
                if (overrideWidth <= 0 && overrideHeight <= 0) {
                    options.sizeMultiplier(sizeMultiplier);
                } else {
                    options.override(overrideWidth, overrideHeight);
                }
                options.diskCacheStrategy(DiskCacheStrategy.ALL);
                options.centerCrop();
                options.placeholder(R.drawable.image_placeholder);
                Glide.with(context)
                        .asBitmap()
                        .load(path)
                        .apply(options)
                        .into(contentHolder.iv_picture);
            }
            if (enablePreview || enablePreviewVideo || enablePreviewAudio) {
                contentHolder.ll_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 如原图路径不存在或者路径存在但文件不存在
                        if (!new File(path).exists()) {
                            ToastManage.s(context, PictureMimeType.s(context, mediaMimeType));
                            return;
                        }
                        changeCheckboxState(contentHolder, image);
                    }
                });
            }
            contentHolder.contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 如原图路径不存在或者路径存在但文件不存在
                    if (!new File(path).exists()) {
                        ToastManage.s(context, PictureMimeType.s(context, mediaMimeType));
                        return;
                    }
                    int index = showCamera ? position - 1 : position;
                    boolean eqResult =
                            mediaMimeType == PictureConfig.TYPE_IMAGE && enablePreview
                                    || mediaMimeType == PictureConfig.TYPE_VIDEO && (enablePreviewVideo
                                    || selectMode == PictureConfig.SINGLE)
                                    || mediaMimeType == PictureConfig.TYPE_AUDIO && (enablePreviewAudio
                                    || selectMode == PictureConfig.SINGLE);
                    if (eqResult) {
                        imageSelectChangedListener.onPictureClick(image, index);
                    } else {
                        changeCheckboxState(contentHolder, image);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return showCamera ? images.size() + 1 : images.size();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        View headerView;
        TextView tv_title_camera;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerView = itemView;
            tv_title_camera = (TextView) itemView.findViewById(R.id.tv_title_camera);
            String title = mimeType == PictureMimeType.ofAudio() ?
                    context.getString(R.string.picture_tape)
                    : context.getString(R.string.picture_take_picture);
            tv_title_camera.setText(title);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_picture;
        TextView check;
        TextView tv_duration, tv_isGif, tv_long_chart;
        View contentView;
        LinearLayout ll_check;

        public ViewHolder(View itemView) {
            super(itemView);
            contentView = itemView;
            iv_picture = (ImageView) itemView.findViewById(R.id.iv_picture);
            check = (TextView) itemView.findViewById(R.id.check);
            ll_check = (LinearLayout) itemView.findViewById(R.id.ll_check);
            tv_duration = (TextView) itemView.findViewById(R.id.tv_duration);
            tv_isGif = (TextView) itemView.findViewById(R.id.tv_isGif);
            tv_long_chart = (TextView) itemView.findViewById(R.id.tv_long_chart);
        }
    }

    public boolean isSelected(LocalMedia image) {
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(image.getPath())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 选择按钮更新
     */
    private void notifyCheckChanged(ViewHolder viewHolder, LocalMedia imageBean) {
        viewHolder.check.setText("");
        for (LocalMedia media : selectImages) {
            if (media.getPath().equals(imageBean.getPath())) {
                imageBean.setNum(media.getNum());
                media.setPosition(imageBean.getPosition());
                viewHolder.check.setText(String.valueOf(imageBean.getNum()));
            }
        }
    }

    /**
     * 改变图片选中状态
     *
     * @param contentHolder
     * @param image
     */

    private void changeCheckboxState(ViewHolder contentHolder, LocalMedia image) {
        boolean isChecked = contentHolder.check.isSelected();
        String pictureType = selectImages.size() > 0 ? selectImages.get(0).getPictureType() : "";
        if (!TextUtils.isEmpty(pictureType)) {
            boolean toEqual = PictureMimeType.mimeToEqual(pictureType, image.getPictureType());
            //TODO 20180819 我修改下面是我注释掉的，可以同时选择视频和图片
//            if (!toEqual) {
//                ToastManage.s(context, context.getString(R.string.picture_rule));
//                return;
//            }
        }
        if (selectImages.size() >= maxSelectNum && !isChecked) {
            boolean eqImg = pictureType.startsWith(PictureConfig.IMAGE);
            String str = eqImg ? context.getString(R.string.picture_message_max_num, maxSelectNum + "")
                    : context.getString(R.string.picture_message_video_max_num, maxSelectNum + "");
            ToastManage.s(context, str);
            return;
        }
        if (!isChecked) {
            int videoCnt = 0;
            for (int i = 0, len = selectImages.size(); i < len; i++) {
                LocalMedia localMedia = selectImages.get(i);
                if (PictureMimeType.isPictureType(localMedia.getPictureType()) == PictureMimeType.ofVideo()) {
                    if (++videoCnt >= config.maxVideoNum) {
                        String str = context.getString(R.string.picture_message_video_max_count, config.maxVideoNum);
                        ToastManage.s(context, str);
                        return;
                    }
                }
            }
            if (config.onlyOneMimeType) {
                if (selectImages != null && selectImages.size() > 0) {
                    LocalMedia localMedia = selectImages.get(0);
                    if (null != localMedia && null != image) {
                        if ((PictureMimeType.isPictureType(localMedia.getPictureType()) == PictureMimeType.ofVideo() && PictureMimeType.isPictureType(image.getPictureType()) != PictureMimeType.ofVideo())) {
                            if (config.maxVideoNum >= 1) {
                                String tip = String.format(Locale.getDefault(), "只能选择至多%d个视频",
                                        config.maxVideoNum);
                                ToastManage.s(context, tip);
                                return;
                            }
                        } else if (((PictureMimeType.isPictureType(localMedia.getPictureType()) == PictureMimeType.ofImage() && PictureMimeType.isPictureType(image.getPictureType()) != PictureMimeType.ofImage()))) {
                            if (config.maxVideoNum >= 1) {
                                String tip = String.format(Locale.getDefault(), "只能选择至多%d张图片",
                                        config.maxSelectNum);
                                ToastManage.s(context, tip);
                            }
                            return;
                        }
                    }
                }
            }
        }
        //TODO 20180819 我修改 限制文件的总大小及单个大小
        if (!isChecked && !TextUtils.isEmpty(image.getPath())) {
            File file = new File(image.getPath());
            if (file.exists()) {
                if (file.length() > singleFileMaxLenth) {
                    String str =
                            context.getString(R.string.picture_message_singleFile_max_lenth) + fileSize(singleFileMaxLenth);
                    ToastManage.s(context, str);
                    return;
                } else {
                    int totalFileMax = 0;
                    for (int i = 0, len = selectImages.size(); i < len; i++) {
                        LocalMedia imageTemp = selectImages.get(i);
                        File fileT = new File(imageTemp.getPath());
                        if (fileT.exists()) {
                            totalFileMax += fileT.length();
                        }
                    }
                    totalFileMax += file.length();
                    if (totalFileMax > totalFileMaxLenth) {
                        String str =
                                context.getString(R.string.picture_message_totalFile_max_lenth) + fileSize(totalFileMaxLenth);
                        ToastManage.s(context, str);
                        return;
                    }
                }
            }
        }

        if (isChecked) {
            for (LocalMedia media : selectImages) {
                if (media.getPath().equals(image.getPath())) {
                    selectImages.remove(media);
                    subSelectPosition();
                    disZoom(contentHolder.iv_picture);
                    break;
                }
            }
        } else {
            // 如果是单选，则清空已选中的并刷新列表(作单一选择)
            if (selectMode == PictureConfig.SINGLE) {
                singleRadioMediaImage();
            }
            selectImages.add(image);
            image.setNum(selectImages.size());
            VoiceUtils.playVoice(context, enableVoice);
            zoom(contentHolder.iv_picture);
        }
        //通知点击项发生了改变
        notifyItemChanged(contentHolder.getAdapterPosition());
        selectImage(contentHolder, !isChecked, true);
        if (imageSelectChangedListener != null) {
            imageSelectChangedListener.onChange(selectImages);
        }
    }

    /**
     * 单选模式
     */
    private void singleRadioMediaImage() {
        if (selectImages != null
                && selectImages.size() > 0) {
            isGo = true;
            LocalMedia media = selectImages.get(0);
            notifyItemChanged(config.isCamera ? media.position :
                    isGo ? media.position : media.position > 0 ? media.position - 1 : 0);
            selectImages.clear();
        }
    }

    /**
     * 更新选择的顺序
     */
    private void subSelectPosition() {
        if (is_checked_num) {
            int size = selectImages.size();
            for (int index = 0, length = size; index < length; index++) {
                LocalMedia media = selectImages.get(index);
                media.setNum(index + 1);
                notifyItemChanged(media.position);
            }
        }
    }

    /**
     * 选中的图片并执行动画
     *
     * @param holder
     * @param isChecked
     * @param isAnim
     */
    public void selectImage(ViewHolder holder, boolean isChecked, boolean isAnim) {
        holder.check.setSelected(isChecked);
        if (isChecked) {
            if (isAnim) {
                if (animation != null) {
                    holder.check.startAnimation(animation);
                }
            }
            holder.iv_picture.setColorFilter(ContextCompat.getColor
                    (context, R.color.image_overlay_true), PorterDuff.Mode.SRC_ATOP);
        } else {
            holder.iv_picture.setColorFilter(ContextCompat.getColor
                    (context, R.color.image_overlay_false), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public interface OnPhotoSelectChangedListener {
        /**
         * 拍照回调
         */
        void onTakePhoto();

        /**
         * 已选Media回调
         *
         * @param selectImages
         */
        void onChange(List<LocalMedia> selectImages);

        /**
         * 图片预览回调
         *
         * @param media
         * @param position
         */
        void onPictureClick(LocalMedia media, int position);
    }

    public void setOnPhotoSelectChangedListener(OnPhotoSelectChangedListener
                                                        imageSelectChangedListener) {
        this.imageSelectChangedListener = imageSelectChangedListener;
    }

    private void zoom(ImageView iv_img) {
        if (zoomAnim) {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(iv_img, "scaleX", 1f, 1.12f),
                    ObjectAnimator.ofFloat(iv_img, "scaleY", 1f, 1.12f)
            );
            set.setDuration(DURATION);
            set.start();
        }
    }

    private void disZoom(ImageView iv_img) {
        if (zoomAnim) {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(iv_img, "scaleX", 1.12f, 1f),
                    ObjectAnimator.ofFloat(iv_img, "scaleY", 1.12f, 1f)
            );
            set.setDuration(DURATION);
            set.start();
        }
    }

    /**
     * @param size 文件大小Byte
     * @return 格式化的文件大小
     */
    public String fileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
