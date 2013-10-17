#pragma once

#include "ProjectManager.h"
#include "RequestModel.h"
#include "PreFeeRequestModel.h"

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace chainofresponsibility
			{
				namespace example5
				{
					///
					/// <summary> * ʵ��Ϊ��Ŀ��������Ԥ֧���÷������봦��Ĺ��ܵ��Ӷ���
					/// * ���ڵ���Ŀ����ȿ��Դ���۲ͷ������룬�ֿ��Դ���Ԥ֧���÷������� </summary>
					/// 
					class ProjectManager2 : public ProjectManager
					{
					public:
						virtual object *handleRequest(RequestModel *rm);
					private:
						object *myHandler(RequestModel *rm);
					};

				}
			}
		}
	}
}