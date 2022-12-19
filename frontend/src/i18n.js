import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import translationsEN from './translations/en.json';
import translationsRU from './translations/ru.json';

import LanguageDetector from 'i18next-browser-languagedetector';

const resources = {
  en: {
    translation: translationsEN,
  },
  ru: {
    translation: translationsRU,
  },
};
i18n
    .use(LanguageDetector)
    .use(initReactI18next)
    .init({
      detection: {
        order: ['navigator'],
      },
      fallbackLng: 'en',
      resources,
    });

export default i18n;
