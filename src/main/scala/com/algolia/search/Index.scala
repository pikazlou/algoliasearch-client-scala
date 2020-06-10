package com.algolia.search

import com.algolia.errors.AlgoliaError
import com.algolia.requester.{Functor, Requester}
import com.algolia.search.Responses.SaveObjectRes
import com.algolia.transport.Call.Write
import com.algolia.transport.Transport._
import com.algolia.transport.{Post, Transport}

class Index[T: Manifest, F[_]](
    transport: Transport,
    indexName: String,
)(
    implicit
    requester: Requester[F],
    functor: Functor[F],
) {

  type Response[U] = F[Either[AlgoliaError, U]]

  private def path(endpoint: String): String = {
    val urlEncodedIndexName = urlEncode(indexName)
    val urlEncodedEndpoint = urlEncode(endpoint)
    s"/1/indexes/$urlEncodedIndexName$urlEncodedEndpoint"
  }

  def saveObject(obj: T): Response[SaveObjectRes] = {
    val p = path("")
    transport.request(Post, p, obj, Write)
  }

}
