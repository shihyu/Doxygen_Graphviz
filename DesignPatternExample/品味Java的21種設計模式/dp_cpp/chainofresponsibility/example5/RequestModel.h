#pragma once

#include <string>

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
					/// <summary> * ͨ�õ�������� </summary>
					/// 
					class RequestModel
					{
					///	
					///	 <summary> * ��ʾ�����ҵ������ </summary>
					///	 
					private:
						std::string type;
					///	
					///	 <summary> * ͨ�����췽���Ѿ����ҵ�����ʹ��ݽ��� </summary>
					///	 * <param name="type"> �����ҵ������ </param>
					///	 
					public:
						RequestModel(std::string type);
						virtual std::string getType();
					};
				}
			}
		}
	}
}