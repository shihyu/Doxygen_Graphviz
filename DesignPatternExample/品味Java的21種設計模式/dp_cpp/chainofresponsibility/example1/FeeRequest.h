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
				namespace example1
				{
					///
					/// <summary> * ����۲ͷ�������Ķ��� </summary>
					/// 
					class FeeRequest
					{
					///	
					///	 <summary> * �ύ�۲ͷ����������Ŀ���� </summary>
					///	 * <param name="user"> ������ </param>
					///	 * <param name="fee"> ������� </param>
					///	 * <returns> �ɹ���ʧ�ܵľ���֪ͨ </returns>
					///	 
					public:
						virtual std::string requestToProjectManager(std::string user, double fee);
					///	
					///	 <summary> * ��Ŀ���������������룬����������ֵ��������һ���ģ�ʡ���� </summary>
					///	 
					private:
						std::string projectHandle(std::string user, double fee);
					///	
					///	 <summary> * ���ž��������������룬����������ֵ��������һ���ģ�ʡ���� </summary>
					///	 
						std::string depManagerHandle(std::string user, double fee);
					///	
					///	 <summary> * �ܾ��������������룬����������ֵ��������һ���ģ�ʡ���� </summary>
					///	 
						std::string generalManagerHandle(std::string user, double fee);

					};

				}
			}
		}
	}
}