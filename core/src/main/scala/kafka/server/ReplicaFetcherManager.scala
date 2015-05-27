/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kafka.server

import kafka.cluster.BrokerEndPoint

import org.apache.kafka.common.protocol.SecurityProtocol


class ReplicaFetcherManager(private val brokerConfig: KafkaConfig, private val replicaMgr: ReplicaManager)
        extends AbstractFetcherManager("ReplicaFetcherManager on broker " + brokerConfig.brokerId,
                                       "Replica", brokerConfig.numReplicaFetchers) {

  override def createFetcherThread(fetcherId: Int, sourceBroker: BrokerEndPoint): AbstractFetcherThread = {
    new ReplicaFetcherThread("ReplicaFetcherThread-%d-%d".format(fetcherId, sourceBroker.id), sourceBroker, brokerConfig, replicaMgr,
                             brokerConfig.interBrokerSecurityProtocol)
  }

  def shutdown() {
    info("shutting down")
    closeAllFetchers()
    info("shutdown completed")
  }
}
