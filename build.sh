#!/bin/bash

#   Copyright (C) 2008,2009 Central Union Of Municipalities & Communities Of Greece (http://www.kedke.gr)
#   Copyright (C) 2008,2009 Profile S.A. (http://www.profile.gr)
#   Copyright (C) 2008,2009 Codex O.E. (http://www.codex.gr)
#   
#   This file is part of the Integrated Management System for Public Projects (IMSPP).
#
#   IMSPP is free software: you can redistribute it and/or modify
#   it under the terms of the GNU General Public License as published by
#   the Free Software Foundation, either version 3 of the License, or
#   (at your option) any later version.
#
#   IMSPP is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#   GNU General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#   along with IMSPP.  If not, see <http://www.gnu.org/licenses/>.
#   
#   Authors:
#     Konstantinos Margaritis <markos@codex.gr>
#     Theodore Karkoulis <t.karkoulis@gmail.com>
#
# $Id: build.sh 1553 2011-12-18 10:13:33Z markos $

ISLOCAL=$1

mvn package
mv target/OTA-1.war target/OTA.war

if [ "$ISLOCAL" == "-l" ]; then
  echo "Doing local copy..."
  cp target/OTA.war /opt/jboss/server/default/deploy/
else
  echo "Doing scp..."
  scp -P 20022 target/OTA.war root@espressocodex.dyndns.org:/opt/jboss/server/default/deploy/
fi
