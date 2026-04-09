(ns xtra-quo.components.core
  (:require [quo.extra.components.above-keyboad-view.view]
            [quo.extra.components.above-keyboard-container.view]
   ;[xtra-quo.components.emoji-picker]
            ))

;; Containers
;; TODO: move this component inside this folder
;; ;; TODO: add a blur layer


(def use-keyboard-height-listener quo.extra.components.above-keyboad-view.view/use-keyboard-height-listener)
(def above-keyboad-view quo.extra.components.above-keyboad-view.view/view)
(def above-keyboard-container quo.extra.components.above-keyboard-container.view/view)

(def emoji-picker nil #_ xtra-quo.components.emoji-picker/view)
