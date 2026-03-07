(ns xquo.foundations.animations)

(def press-feedback
  {:transition-property     "transform"
   :default-duration        "220ms"
   :default-timing-function "ease"
   :default-scale           1
   :default-translate-y     0
   :pressed-duration        "120ms"
   :pressed-timing-function "ease-out"
   :pressed-scale           0.985
   :pressed-translate-y     2})

(def state-change
  {:transition-property        "transform"
   :transition-timing-function "ease-in-out"
   :toggle-duration            "300ms"
   :radio-expand-duration      "300ms"
   :radio-collapse-duration    "200ms"})
