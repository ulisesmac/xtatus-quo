#import "MTREffectView.h"

#import <React/RCTBorderDrawing.h>
#import <React/RCTConversions.h>
#import <react/renderer/components/XquoSpec/ComponentDescriptors.h>
#import <react/renderer/components/XquoSpec/Props.h>
#import <react/renderer/components/XquoSpec/RCTComponentViewHelpers.h>

using namespace facebook::react;

static RCTCornerRadii MTREffectCornerRadiiFromBorderRadii(BorderRadii borderRadii)
{
  return RCTCornerRadii{
      .topLeftHorizontal = (CGFloat)borderRadii.topLeft.horizontal,
      .topLeftVertical = (CGFloat)borderRadii.topLeft.vertical,
      .topRightHorizontal = (CGFloat)borderRadii.topRight.horizontal,
      .topRightVertical = (CGFloat)borderRadii.topRight.vertical,
      .bottomLeftHorizontal = (CGFloat)borderRadii.bottomLeft.horizontal,
      .bottomLeftVertical = (CGFloat)borderRadii.bottomLeft.vertical,
      .bottomRightHorizontal = (CGFloat)borderRadii.bottomRight.horizontal,
      .bottomRightVertical = (CGFloat)borderRadii.bottomRight.vertical};
}

static BOOL MTREffectHasCornerRadii(RCTCornerRadii cornerRadii)
{
  return cornerRadii.topLeftHorizontal > 0 ||
      cornerRadii.topLeftVertical > 0 ||
      cornerRadii.topRightHorizontal > 0 ||
      cornerRadii.topRightVertical > 0 ||
      cornerRadii.bottomLeftHorizontal > 0 ||
      cornerRadii.bottomLeftVertical > 0 ||
      cornerRadii.bottomRightHorizontal > 0 ||
      cornerRadii.bottomRightVertical > 0;
}

@interface MTREffectView () <RCTMTREffectViewViewProtocol>
@end

@implementation MTREffectView {
  UIViewPropertyAnimator *_blurAnimator;
  UIVisualEffectView *_effectView;
  CAShapeLayer *_effectMaskLayer;
  CAShapeLayer *_effectContentMaskLayer;
  UIColor *_effectTintColor;
}

- (instancetype)initWithFrame:(CGRect)frame
{
  if (self = [super initWithFrame:frame]) {
    static const auto defaultProps = std::make_shared<const MTREffectViewProps>();
    _props = defaultProps;

    _effectView = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleSystemMaterial]];
    _effectView.frame = self.bounds;
    _effectView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [self addSubview:_effectView];
    [self updateNativeEffectWithProps:*defaultProps];
  }

  return self;
}

- (void)setBackgroundColor:(UIColor *)backgroundColor
{
  _effectTintColor = backgroundColor;

  if (_effectView) {
    const auto &props = *std::static_pointer_cast<MTREffectViewProps const>(_props);
    [self applyTintColorWithProps:props];
    [super setBackgroundColor:UIColor.clearColor];
    return;
  }

  [super setBackgroundColor:backgroundColor];
}

- (void)layoutSubviews
{
  [super layoutSubviews];
  _effectView.frame = self.bounds;
  [self updateEffectMask];
}

- (void)didMoveToWindow
{
  [super didMoveToWindow];

  if (!self.window) {
    return;
  }

  dispatch_async(dispatch_get_main_queue(), ^{
    if (!self.window) {
      return;
    }

    const auto &props = *std::static_pointer_cast<MTREffectViewProps const>(self->_props);
    [self updateNativeEffectWithProps:props];
  });
}

- (void)updateEffectTintColorWithProps:(const MTREffectViewProps &)props
{
  _effectTintColor = props.backgroundColor ? RCTUIColorFromSharedColor(props.backgroundColor) : nil;
}

- (void)mountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index
{
  [_effectView.contentView insertSubview:childComponentView atIndex:index];
}

- (void)unmountChildComponentView:(UIView<RCTComponentViewProtocol> *)childComponentView index:(NSInteger)index
{
  [childComponentView removeFromSuperview];
}

- (void)updateProps:(Props::Shared const &)props oldProps:(Props::Shared const &)oldProps
{
  const auto &oldViewProps = *std::static_pointer_cast<MTREffectViewProps const>(_props);
  const auto &newViewProps = *std::static_pointer_cast<MTREffectViewProps const>(props);
  const BOOL needsEffectMaskUpdate =
      oldViewProps.borderRadii != newViewProps.borderRadii ||
      oldViewProps.effect != newViewProps.effect;
  const BOOL needsEffectTintUpdate = oldViewProps.backgroundColor != newViewProps.backgroundColor;

  [super updateProps:props oldProps:oldProps];

  if (needsEffectTintUpdate ||
      oldViewProps.blurIntensity != newViewProps.blurIntensity ||
      oldViewProps.effect != newViewProps.effect ||
      oldViewProps.intensity != newViewProps.intensity ||
      oldViewProps.theme != newViewProps.theme ||
      oldViewProps.interactive != newViewProps.interactive) {
    [self updateNativeEffectWithProps:newViewProps];
  } else {
    [self applyTintColorWithProps:newViewProps];
  }

  if (needsEffectMaskUpdate) {
    [self updateEffectMask];
  }
}

- (void)updateLayoutMetrics:(const LayoutMetrics &)layoutMetrics oldLayoutMetrics:(const LayoutMetrics &)oldLayoutMetrics
{
  [super updateLayoutMetrics:layoutMetrics oldLayoutMetrics:oldLayoutMetrics];
  [self updateEffectMask];
}

+ (ComponentDescriptorProvider)componentDescriptorProvider
{
  return concreteComponentDescriptorProvider<MTREffectViewComponentDescriptor>();
}

- (void)updateNativeEffectWithProps:(const MTREffectViewProps &)props
{
  [self updateEffectTintColorWithProps:props];

  if (UIAccessibilityIsReduceTransparencyEnabled()) {
    [self clearBlurAnimator];
    _effectView.effect = nil;
    _effectView.contentView.backgroundColor = _effectTintColor ?: UIColor.clearColor;
    return;
  }

  NSString *effect = [NSString stringWithUTF8String:toString(props.effect).c_str()];
  NSString *intensity = [NSString stringWithUTF8String:toString(props.intensity).c_str()];
  NSString *theme = [NSString stringWithUTF8String:toString(props.theme).c_str()];
  [self applyUserInterfaceStyleForTheme:theme];

  if ([effect isEqualToString:@"glass"]) {
    if (@available(iOS 26.0, *)) {
      [self clearBlurAnimator];
      UIGlassEffectStyle style = [intensity isEqualToString:@"clear"]
        ? UIGlassEffectStyleClear
        : UIGlassEffectStyleRegular;
      UIGlassEffect *glassEffect = [UIGlassEffect effectWithStyle:style];
      glassEffect.interactive = props.interactive;
      glassEffect.tintColor = _effectTintColor;
      _effectView.effect = glassEffect;
      _effectView.contentView.backgroundColor = UIColor.clearColor;
      return;
    }

    [self applyBlurEffectWithStyle:[self blurFallbackStyleForGlassIntensity:intensity theme:theme]
                      blurIntensity:100];
    _effectView.contentView.backgroundColor = _effectTintColor ?: UIColor.clearColor;
    return;
  }

  [self applyBlurEffectWithStyle:[self blurStyleForIntensity:intensity theme:theme]
                    blurIntensity:props.blurIntensity];
  _effectView.contentView.backgroundColor = _effectTintColor ?: UIColor.clearColor;
}

- (void)applyUserInterfaceStyleForTheme:(NSString *)theme
{
  UIUserInterfaceStyle userInterfaceStyle = UIUserInterfaceStyleUnspecified;

  if ([theme isEqualToString:@"light"]) {
    userInterfaceStyle = UIUserInterfaceStyleLight;
  } else if ([theme isEqualToString:@"dark"]) {
    userInterfaceStyle = UIUserInterfaceStyleDark;
  }

  self.overrideUserInterfaceStyle = userInterfaceStyle;
  _effectView.overrideUserInterfaceStyle = userInterfaceStyle;
  _effectView.contentView.overrideUserInterfaceStyle = userInterfaceStyle;
}

- (void)clearBlurAnimator
{
  if (_blurAnimator) {
    [_blurAnimator stopAnimation:YES];
    _blurAnimator = nil;
  }
}

- (void)applyBlurEffectWithStyle:(UIBlurEffectStyle)style blurIntensity:(int)blurIntensity
{
  [self clearBlurAnimator];

  CGFloat normalizedIntensity = MIN(MAX((CGFloat)blurIntensity, 0), 100) / 100.0;

  if (normalizedIntensity <= 0) {
    _effectView.effect = nil;
    return;
  }

  UIBlurEffect *blurEffect = [UIBlurEffect effectWithStyle:style];

  if (normalizedIntensity >= 1) {
    _effectView.effect = blurEffect;
    return;
  }

  _effectView.effect = nil;
  _blurAnimator = [[UIViewPropertyAnimator alloc] initWithDuration:1
                                                             curve:UIViewAnimationCurveLinear
                                                        animations:^{
                                                          self->_effectView.effect = blurEffect;
                                                        }];
  [_blurAnimator startAnimation];
  [_blurAnimator pauseAnimation];
  _blurAnimator.fractionComplete = normalizedIntensity;
}

- (void)applyTintColorWithProps:(const MTREffectViewProps &)props
{
  NSString *effect = [NSString stringWithUTF8String:toString(props.effect).c_str()];

  if ([effect isEqualToString:@"glass"]) {
    if (@available(iOS 26.0, *)) {
      if ([_effectView.effect isKindOfClass:[UIGlassEffect class]]) {
        ((UIGlassEffect *)_effectView.effect).tintColor = _effectTintColor;
      }
      _effectView.contentView.backgroundColor = UIColor.clearColor;
      return;
    }
  }

  _effectView.contentView.backgroundColor = _effectTintColor ?: UIColor.clearColor;
}

- (void)updateEffectMask
{
  if (!_effectView || CGRectIsEmpty(_effectView.bounds)) {
    [self resetEffectCornerConfiguration];
    _effectView.clipsToBounds = NO;
    _effectView.layer.mask = nil;
    _effectView.contentView.clipsToBounds = NO;
    _effectView.contentView.layer.mask = nil;
    return;
  }

  const auto borderMetrics = _props->resolveBorderMetrics(_layoutMetrics);
  const RCTCornerRadii cornerRadii = MTREffectCornerRadiiFromBorderRadii(borderMetrics.borderRadii);
  const auto &props = *std::static_pointer_cast<MTREffectViewProps const>(_props);
  NSString *effect = [NSString stringWithUTF8String:toString(props.effect).c_str()];
  BOOL usesGlassCornerConfiguration = NO;

  if (@available(iOS 26.0, *)) {
    usesGlassCornerConfiguration = [effect isEqualToString:@"glass"];
  }

  if (!MTREffectHasCornerRadii(cornerRadii)) {
    [self resetEffectCornerConfiguration];
    _effectView.clipsToBounds = NO;
    _effectView.layer.cornerRadius = 0;
    _effectView.layer.mask = nil;
    _effectView.contentView.clipsToBounds = NO;
    _effectView.contentView.layer.cornerRadius = 0;
    _effectView.contentView.layer.mask = nil;
    return;
  }

  if (usesGlassCornerConfiguration) {
    [self applyEffectCornerConfiguration:cornerRadii];
  } else {
    [self resetEffectCornerConfiguration];
  }

  _effectView.clipsToBounds = YES;
  _effectView.contentView.clipsToBounds = YES;

  if (RCTCornerRadiiAreEqualAndSymmetrical(cornerRadii)) {
    CGFloat cornerRadius = cornerRadii.topLeftHorizontal;
    _effectView.layer.cornerRadius = cornerRadius;
    _effectView.contentView.layer.cornerRadius = cornerRadius;
    _effectView.layer.mask = nil;
    _effectView.contentView.layer.mask = nil;
    return;
  }

  if (!_effectMaskLayer) {
    _effectMaskLayer = [CAShapeLayer layer];
  }
  if (!_effectContentMaskLayer) {
    _effectContentMaskLayer = [CAShapeLayer layer];
  }

  CGPathRef maskPath = RCTPathCreateWithRoundedRect(
      _effectView.bounds,
      RCTGetCornerInsets(cornerRadii, UIEdgeInsetsZero),
      nil,
      NO);

  [CATransaction begin];
  [CATransaction setDisableActions:YES];
  _effectView.layer.cornerRadius = 0;
  _effectView.contentView.layer.cornerRadius = 0;
  _effectMaskLayer.frame = _effectView.bounds;
  _effectMaskLayer.path = maskPath;
  _effectMaskLayer.fillColor = UIColor.blackColor.CGColor;
  _effectView.layer.mask = _effectMaskLayer;

  _effectContentMaskLayer.frame = _effectView.contentView.bounds;
  _effectContentMaskLayer.path = maskPath;
  _effectContentMaskLayer.fillColor = UIColor.blackColor.CGColor;
  _effectView.contentView.layer.mask = _effectContentMaskLayer;
  [CATransaction commit];

  CGPathRelease(maskPath);
}

- (void)resetEffectCornerConfiguration
{
  if (@available(iOS 26.0, *)) {
    UICornerConfiguration *configuration =
        [UICornerConfiguration configurationWithRadius:[UICornerRadius fixedRadius:0]];
    _effectView.cornerConfiguration = configuration;
    _effectView.contentView.cornerConfiguration = configuration;
  }
}

- (void)applyEffectCornerConfiguration:(RCTCornerRadii)cornerRadii
{
  if (@available(iOS 26.0, *)) {
    UICornerConfiguration *configuration;

    if (RCTCornerRadiiAreEqualAndSymmetrical(cornerRadii)) {
      configuration = [UICornerConfiguration
          configurationWithUniformRadius:[UICornerRadius fixedRadius:cornerRadii.topLeftHorizontal]];
    } else {
      configuration = [UICornerConfiguration
          configurationWithTopLeftRadius:[UICornerRadius fixedRadius:cornerRadii.topLeftHorizontal]
                          topRightRadius:[UICornerRadius fixedRadius:cornerRadii.topRightHorizontal]
                        bottomLeftRadius:[UICornerRadius fixedRadius:cornerRadii.bottomLeftHorizontal]
                       bottomRightRadius:[UICornerRadius fixedRadius:cornerRadii.bottomRightHorizontal]];
    }

    _effectView.cornerConfiguration = configuration;
    _effectView.contentView.cornerConfiguration = configuration;
  }
}

- (UIBlurEffectStyle)blurFallbackStyleForGlassIntensity:(NSString *)intensity theme:(NSString *)theme
{
  if ([intensity isEqualToString:@"clear"]) {
    return [self themedBlurStyleForTheme:theme
                             systemStyle:UIBlurEffectStyleSystemUltraThinMaterial
                              lightStyle:UIBlurEffectStyleSystemUltraThinMaterialLight
                               darkStyle:UIBlurEffectStyleSystemUltraThinMaterialDark];
  }

  return [self themedBlurStyleForTheme:theme
                           systemStyle:UIBlurEffectStyleSystemMaterial
                            lightStyle:UIBlurEffectStyleSystemMaterialLight
                             darkStyle:UIBlurEffectStyleSystemMaterialDark];
}

- (UIBlurEffectStyle)blurStyleForIntensity:(NSString *)intensity theme:(NSString *)theme
{
  if ([intensity isEqualToString:@"ultra-thin"]) {
    return [self themedBlurStyleForTheme:theme
                             systemStyle:UIBlurEffectStyleSystemUltraThinMaterial
                              lightStyle:UIBlurEffectStyleSystemUltraThinMaterialLight
                               darkStyle:UIBlurEffectStyleSystemUltraThinMaterialDark];
  }

  if ([intensity isEqualToString:@"thin"]) {
    return [self themedBlurStyleForTheme:theme
                             systemStyle:UIBlurEffectStyleSystemThinMaterial
                              lightStyle:UIBlurEffectStyleSystemThinMaterialLight
                               darkStyle:UIBlurEffectStyleSystemThinMaterialDark];
  }

  if ([intensity isEqualToString:@"thick"]) {
    return [self themedBlurStyleForTheme:theme
                             systemStyle:UIBlurEffectStyleSystemThickMaterial
                              lightStyle:UIBlurEffectStyleSystemThickMaterialLight
                               darkStyle:UIBlurEffectStyleSystemThickMaterialDark];
  }

  if ([intensity isEqualToString:@"chrome"]) {
    return [self themedBlurStyleForTheme:theme
                             systemStyle:UIBlurEffectStyleSystemChromeMaterial
                              lightStyle:UIBlurEffectStyleSystemChromeMaterialLight
                               darkStyle:UIBlurEffectStyleSystemChromeMaterialDark];
  }

  return [self themedBlurStyleForTheme:theme
                           systemStyle:UIBlurEffectStyleSystemMaterial
                            lightStyle:UIBlurEffectStyleSystemMaterialLight
                             darkStyle:UIBlurEffectStyleSystemMaterialDark];
}

- (UIBlurEffectStyle)themedBlurStyleForTheme:(NSString *)theme
                                 systemStyle:(UIBlurEffectStyle)systemStyle
                                  lightStyle:(UIBlurEffectStyle)lightStyle
                                   darkStyle:(UIBlurEffectStyle)darkStyle
{
  if ([theme isEqualToString:@"light"]) {
    return lightStyle;
  }

  if ([theme isEqualToString:@"dark"]) {
    return darkStyle;
  }

  return systemStyle;
}

@end
