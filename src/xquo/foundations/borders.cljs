(ns xquo.foundations.borders)

;; TODO: rename to avoid the borders word and also use it like a FN instead of as a value
(def border-radius-values
  {:border/max           "50%" ;; Contextual
   :border/drawer        20 ;; Drawers
   :border/card-section  16 ;; Cards, Sections
   :border/sizes-40-56   12 ;; 40px - 56px height
   :border/size-32       10 ;; >= 32px height
   :border/size-24       8 ;; >= 24px height
   :border/size-16       6 ;; >= 16px height
   :border/size-12       4 ;; >= 12px height
   :border/bounding-area 0}) ;; Bounding Areas
