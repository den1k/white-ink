(ns white-ink.state
  (:require [white-ink.utils.state :refer [make-squuid]]))

(def draft
  "It turned out that there was something terribly stressful about visual telephone interfaces that hadn’t been stressful at all about voice-only interfaces. Videophone consumers seemed suddenly to realize that they’d been subject to an insidious but wholly marvelous delusion about conventional voice-only telephony. They’d never noticed it before, the delusion — it’s like it was so emotionally complex that it could be countenanced only in the context of its loss. Good old traditional audio-only phone conversations allowed you to presume that the person on the other end was paying complete attention to you while also permitting you not to have to pay anything even close to complete attention to her. A traditional aural-only conversation — utilizing a hand- held phone whose earpiece contained only 6 little pinholes but whose mouthpiece (rather significantly, it later seemed) contained (62) or 36 little pinholes — let you enter a kind of highway-hypnotic semi-attentive fugue: while conversing, you could look around the room, doodle, fine-groom, peel tiny bits of dead skin away from your cuticles, compose phone-pad haiku, stir things on the stove; you could even carry on a whole separate additional sign-language-and-exaggerated-facial-expression type of conversation with people right there in the room with you, all while seeming to be right there attending closely to the voice on the phone. And yet — and this was the retrospectively marvelous part — even as you were dividing your attention between the phone call and all sorts of other idle little fuguelike activities, you were somehow never haunted by the suspicion that the person on the other end’s attention might be similarly divided. During a traditional call, e.g., as you let’s say performed a close tactile blemish- scan of your chin, you were in no way oppressed by the thought that your phonemate was perhaps also devoting a good percentage of her attention to a close tactile blemish-scan. It was an illusion and the illusion was aural and aurally supported: the phone-line’s other end’s voice was dense, tightly compressed, and vectored right into your ear, enabling you to imagine that the voice’s owner’s attention was similarly compressed and focused . . . even though your own attention was not, was the thing. This bilateral illusion of unilateral attention was almost infantilely gratifying from an emotional standpoint: you got to believe you were receiving somebody’s complete attention without having to return it. Regarded with the objectivity of hindsight, the illusion appears arational, almost literally fantastic: it would be like being able both to lie and to trust other people at the same time. Why—though in the early days of Interlace’s internetted teleputers that operated off largely the same fiber-digital grid as the phone companies, the advent of video telephoning (a.k.a. ‘videophony’) enjoyed an interval of huge consumer popularity—callers thrilled at the idea of phone-interfacing both aurally and facially (the little first-generation phone-video cameras being too crude and narrow-apertured for anything much more than facial close-ups) on first-generation teleputers that at that time were little more than high-tech TV sets, though of course they had that little ‘intelligent-agent’ homuncular icon that would appear at the lower-right of a broadcast/cable program and tell you the time and temperature outside or remind you to take your blood-pressure medication or alert you to a particularly compelling entertainment-option now coming up on channel like 491 or something, or of course now alerting you to an incoming video-phone call and then tap-dancing with a little iconic straw boater and cane just under a menu of possible options for response, and callers did lover their little homuncular icons—but why, within like 16 months or 5 sales quarters, the tumescent demand curve for ‘videophony’ suddenly collapsed like a kicked tent, so that, by the year of the depend adult undergarment, fewer than 10% of all private telephone communications utilized any video-image-fiber data-transfers or coincident products and services, the average U.S. phone-user deciding that s/he actually preferred the retrograde old low-tech bell-era voice-only telephonic interface after all, a preferential about-face that cost a good many precipitant video-telephony-related entrepreneurs their shirts, plus destabilizing two highly respected mutual funds that had ground-floored heavily in video-phone technology, and very nearly wiping out the Maryland State employees’ retirement system’s Freddie-Mac fund, a fund whose administrator’s mistress’s brother had been an almost manically precipitant video-phone-technology entrepreneur… and but so why the abrupt consumer retreat back to good old voice-only telephoning? It turned out that there was something terribly stressful about visual telephone interfaces that hadn’t been stressful at all about voice-only interfaces. Videophone consumers seemed suddenly to realize that they’d been subject to an insidious but wholly marvelous delusion about conventional voice-only telephony. They’d never noticed it before, the delusion — it’s like it was so emotionally complex that it could be countenanced only in the context of its loss. Good old traditional audio-only phone conversations allowed you to presume that the person on the other end was paying complete attention to you while also permitting you not to have to pay anything even close to complete attention to her. A traditional aural-only conversation — utilizing a hand- held phone whose earpiece contained only 6 little pinholes but whose mouthpiece (rather significantly, it later seemed) contained (62) or 36 little pinholes — let you enter a kind of highway-hypnotic semi-attentive fugue: while conversing, you could look around the room, doodle, fine-groom, peel tiny bits of dead skin away from your cuticles, compose phone-pad haiku, stir things on the stove; you could even carry on a whole separate additional sign-language-and-exaggerated-facial-expression type of conversation with people right there in the room with you, all while seeming to be right there attending closely to the voice on the phone. And yet — and this was the retrospectively marvelous part — even as you were dividing your attention between the phone call and all sorts of other idle little fuguelike activities, you were somehow never haunted by the suspicion that the person on the other end’s attention might be similarly divided. During a traditional call, e.g., as you let’s say performed a close tactile blemish- scan of your chin, you were in no way oppressed by the thought that your phonemate was perhaps also devoting a good percentage of her attention to a close tactile blemish-scan. It was an illusion and the illusion was aural and aurally supported: the phone-line’s other end’s voice was dense, tightly compressed, and vectored right into your ear, enabling you to imagine that the voice’s owner’s attention was similarly compressed and focused . . . even though your own attention was not, was the thing. This bilateral illusion of unilateral attention was almost infantilely gratifying from an emotional standpoint: you got to believe you were receiving somebody’s complete attention without having to return it. Regarded with the objectivity of hindsight, the illusion appears arational, almost literally fantastic: it would be like being able both to lie and to trust other people at the same time. Why—though in the early days of Interlace’s internetted teleputers that operated off largely the same fiber-digital grid as the phone companies, the advent of video telephoning (a.k.a. ‘videophony’) enjoyed an interval of huge consumer popularity—callers thrilled at the idea of phone-interfacing both aurally and facially (the little first-generation phone-video cameras being too crude and narrow-apertured for anything much more than facial close-ups) on first-generation teleputers that at that time were little more than high-tech TV sets, though of course they had that little ‘intelligent-agent’ homuncular icon that would appear at the lower-right of a broadcast/cable program and tell you the time and temperature outside or remind you to take your blood-pressure medication or alert you to a particularly compelling entertainment-option now coming up on channel like 491 or something, or of course now alerting you to an incoming video-phone call and then tap-dancing with a little iconic straw boater and cane just under a menu of possible options for response, and callers did lover their little homuncular icons—but why, within like 16 months or 5 sales quarters, the tumescent demand curve for ‘videophony’ suddenly collapsed like a kicked tent, so that, by the year of the depend adult undergarment, fewer than 10% of all private telephone communications utilized any video-image-fiber data-transfers or coincident products and services, the average U.S. phone-user deciding that s/he actually preferred the retrograde old low-tech bell-era voice-only telephonic interface after all, a preferential about-face that cost a good many precipitant video-telephony-related entrepreneurs their shirts, plus destabilizing two highly respected mutual funds that had ground-floored heavily in video-phone technology, and very nearly wiping out the Maryland State employees’ retirement system’s Freddie-Mac fund, a fund whose administrator’s mistress’s brother had been an almost manically precipitant video-phone-technology entrepreneur… and but so why the abrupt consumer retreat back to good old voice-only telephoning?")

(def session-2
  "I am session number 2, session number 2. Session TWO TWO TWO. This truly is the second session. I swear to you, charles choplin. This can go on forever. An ever and ever, being session # 2.")

(defn- notes-gen
  ([n draft-text] (notes-gen n draft-text ""))
  ([n draft-text infix]
   (let [draft-idxs (sort (repeatedly n #(rand-int (count draft-text))))]
     (vec (map (fn [count idx]
                 (hash-map :id (make-squuid)
                           :text (str "note-" infix "-idx" #_idx #_count)
                           :draft-index idx))
               (range 1 (inc n))
               draft-idxs)))))

(def mock-notes-first-session (notes-gen 5 draft "first session"))
(def mock-notes-second-session (notes-gen 5 session-2 "second session"))

(def inserts-text
  {:current "I'm the current insert, the one and only current insert bro."
   :first   "First instert in current-session"
   :second  "second instert in current-session"
   :third   "third instert in current-session"})

(defn repeat-string [n x]
  (clojure.string/join (take n (repeat x))))

(def ses-ins-text
  {:first  {:first (repeat-string 10 "-11-")}
   :second {:first  (repeat-string 20 "-21-")
            :second (repeat-string 30 "-22-")}
   :third  {:first (repeat-string 40 "-31-")}})


;; define your app data so that it doesn't get over-written on reload
(def app-state (atom {:user              {:settings {:text-grain false}
                                          ;; metrics would be individual stats about a user calculated over time
                                          :metrics  {:avg-typing-speed 200}}
                      :searching?        false
                      :text-fade-delay   45000
                      :review-scroll-top 2000
                      :speed->opacity    1
                      :current-draft     {:current-session {:current-insert {:start-idx 100 ;where insert started in draft
                                                                             :text      (:current inserts-text) ; insert text
                                                                             :removed?  0
                                                                             :notes     (notes-gen 2 (:current inserts-text) "cur ses cur ins")} ; number of characters removed - if any
                                                            ;; inserts are not visible until the end of the session, when it is added to `sessions`
                                                            :inserts        [{:start-idx 0 ;where insert started in draft
                                                                              :text      (:first inserts-text) ; insert text
                                                                              :removed?  0
                                                                              :notes     (notes-gen 2 (:first inserts-text) "cur ses 1st ins")}
                                                                             {:start-idx 0 ;where insert started in draft
                                                                              :text      (:second inserts-text) ; insert text
                                                                              :removed?  0
                                                                              :notes     (notes-gen 2 (:second inserts-text) "cur ses 2nd ins")}
                                                                             {:start-idx 0 ;where insert started in draft
                                                                              :text      (:third inserts-text) ; insert text
                                                                              :removed?  0
                                                                              :notes     (notes-gen 2 (:third inserts-text) "cur ses 3rd ins")}]}
                                          ;; todo if any later insert has a start-idx smaller than the notes draft-idx,
                                          ;; todo insert, add text length of insert to draft-idx
                                          :sessions        [{:inserts [{:start-idx 0
                                                                        :text      (-> ses-ins-text :first :first)
                                                                        :removed?  0
                                                                        :notes     (map #(assoc % :draft-index (+ 80 15)) (notes-gen 1 (-> ses-ins-text :first :first) "11"))}]}
                                                            {:inserts [{:start-idx 10
                                                                        :text      (-> ses-ins-text :second :first)
                                                                        :removed?  0
                                                                        :notes     (map #(update % :draft-index + 40) (notes-gen 1 (-> ses-ins-text :second :first) "21"))}
                                                                       {:start-idx 20
                                                                        :text      (-> ses-ins-text :second :second)
                                                                        :removed?  0
                                                                        :notes     (map #(update % :draft-index + 80) (notes-gen 1 (-> ses-ins-text :second :second) "22"))}
                                                                       {:start-idx 15
                                                                        :text      (-> ses-ins-text :third :first)
                                                                        :removed?  0
                                                                        ;; todo per inserts coll sort by start-idx, then add total inserts length of all previous inserts
                                                                        :notes     (map #(assoc % :draft-index (+ 80 #_120 5)) (notes-gen 1 (-> ses-ins-text :third :first) "31"))}]
                                                             ;; draft index needs to be saved as sum of current index in inserts and start-idx of insert
                                                             ;; order is all that matters
                                                             ;; to build either the text or the notes all previous operations need to be applied
                                                             ;; in the order in which they where made

                                                             ;; when the current session is finished inserts have to be sorted by start-idx,
                                                             ;; then to each start-idx the length of the previous insert needs to be added
                                                             ;; todo put notes into each insert (otherwise their order cannot be easily determined)
                                                             ;; todo - in that case draft-idx can be relative to insert start-idx
                                                             }]}
                      :drafts            [; this would contain "other" drafts, i.e. entirely different writing projects
                                          ]}))



