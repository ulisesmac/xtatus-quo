(ns utils.worklets.record-audio)

(def ^:private record-audio-worklets #js{})

(defn ring-scale
  [scale substract]
  (.ringScale ^js record-audio-worklets scale substract))
