
const state = {
  venueId: ''
}

const mutations = {
  SET_VENUE_ID: (state, venueId) => {
    state.venueId = venueId
  }
}

const actions = {
  changeVenueId({ commit }, venueId) {
    commit('SET_VENUE_ID', venueId)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

