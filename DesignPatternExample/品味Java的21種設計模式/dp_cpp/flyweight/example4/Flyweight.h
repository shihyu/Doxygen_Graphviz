#pragma once

namespace cn
{
	namespace javass
	{
		namespace dp
		{
			namespace flyweight
			{
				namespace example4
				{


					///<summary> *
					/// * ������Ȩ���ݵ���Ԫ�ӿ� </summary>
					/// 
					class Flyweight
					{
					///	
					///	 <summary> * �жϴ���İ�ȫʵ���Ȩ�ޣ��Ƿ����Ԫ�����ڲ�״̬ƥ�� </summary>
					///	 * <param name="securityEntity"> ��ȫʵ�� </param>
					///	 * <param name="permit"> Ȩ�� </param>
					///	 * <returns> true��ʾƥ�䣬false��ʾ��ƥ�� </returns>
					///	 
					public:
						virtual public bool match(std::string, std::string) = 0;
					///	
					///	 <summary> * Ϊflyweight�����flyweight���� </summary>
					///	 * <param name="f"> ����ӵ���flyweight���� </param>
					///	 
						virtual public void add(Flyweight*) = 0;
					};
				}
			}
		}
	}
}