(ns clojuretodo.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      [stylefy.core :as stylefy :refer [use-style]]))

; init stylefy
(stylefy/init)

; Styles

(def listItemStyle {:display :flex
                    :align-items :center
                    :border :none
                    :background :none
                    :width "100%"
                    :justify-content :space-between
                    :cursor :pointer
                    :margin-top "4px"
                    :padding "16px"
                    :box-shadow "0 2px 4px rgba(2, 14, 29, 0.08)"
                    ::stylefy/mode {:hover {:box-shadow "0 4px 6px rgba(2, 14, 29, 0.08)!important"}}})

(def listItemTextStyle {:font-size "16px"
                        :line-height "20px"
                        :font-weight "600"
                        :color "#1F1F1F"})

(def listItemCheckboxStyle {::stylefy/mode {:checked {:background-color "#4DD488"}}})

(def inputFormContainerStyle {:display :flex
                              :align-items :center
                              :margin "8px 0"})

(def inputStyle {:line-height "20px"})

(def buttonStyle {:cursor "pointer"
                  :margin-left "8px"
                  :padding "4px 16px"
                  :border "none"
                  :background-color "#A161BF"
                  :color "white"
                  :line-height "18px"
                  :border-radius "4px"
                  :transition ".1s all ease"
                  ::stylefy/mode [[:hover {:background-color "#000"}]]})

;; -------------------------
;; State

; atom is like state, change will render re-render
(def taskList (r/atom
               [{:text "Grocery shopping" :completed false}
                {:text "Cook dinner" :completed true}
                {:text "Work out" :completed false}]))

;; -------------------------
;; Components

(defn TodoItem [todo]
  [:button (use-style listItemStyle {:on-click (fn [e]
                                                 (js/console.log "clicked")
                                                 (map (fn [txt]
                                                        (js/console.log txt)) ["1" "2"]))})
   [:div (use-style listItemTextStyle) (:text todo)]
   [:input (use-style listItemCheckboxStyle {:type "checkbox"
                                             :checked (:completed todo)})]
   ])

(defn TodoForm []
  (let [newTask (r/atom "")]
    (fn []
      [:div (use-style inputFormContainerStyle)
        [:input (use-style inputStyle
                           {:type "text"
                            :value @newTask
                            :placeholder "Add a new task"
                            :on-change (fn [e]
                                          (reset! newTask
                                                  (.-value (.-target e))))})]

        [:button (use-style buttonStyle {:on-click (fn [e]
                                         (.preventDefault e)
                                         (swap! taskList conj {:completed false :text @newTask})
                                         (reset! newTask ""))})
           "Add"]]))) ; .-attribute; .callFunction

;; -------------------------
;; Views

(defn home-page []
  [:div (use-style {:padding "8px"})
   [:h3 "Tasks Todo"]
   [TodoForm]
   [:div
    (for [task @taskList]
      (if-not (:completed task) (TodoItem task)))]

   [:h3 (use-style {:margin-top "40px"}) "Completed"]
   [:div
    (for [task @taskList]
      (if (:completed task) (TodoItem task)))]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
